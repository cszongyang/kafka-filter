package com.kafkafilter.filter;

import com.kafkafilter.compiler.DynamicCodeCompiler;
import com.kafkafilter.compiler.GroovyCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author Zongyang Li
 */
public class FilterLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FilterLoader.class);

    private final ConcurrentHashMap<String, Long> filterClassLastModified = new ConcurrentHashMap<String, Long>();
    private final ConcurrentHashMap<String, String> filterClassCode = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, String> filterCheck = new ConcurrentHashMap<String, String>();
//    private final ConcurrentHashMap<FilterType, List<Filter>> hashFiltersByType = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Filter> filtersByName = new ConcurrentHashMap<>();

    private final FilterRegistry filterRegistry;

    private final DynamicCodeCompiler compiler;

    private final FilterFactory filterFactory;

    public FilterLoader() {
        this(new FilterRegistry(), new GroovyCompiler(), new DefaultFilterFactory());
    }
    
    public FilterLoader(FilterRegistry filterRegistry, DynamicCodeCompiler compiler, FilterFactory filterFactory) {
        this.filterRegistry = filterRegistry;
        this.compiler = compiler;
        this.filterFactory = filterFactory;
    }

    /**
     * Given source and name will compile and store the filter if it detects that the filter code has changed or
     * the filter doesn't exist. Otherwise it will return an instance of the requested Filter
     *
     * @param sCode source code
     * @param sName name of the filter
     * @return the IFilter
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Filter getFilter(String sCode, String sName) throws Exception {

        if (filterCheck.get(sName) == null) {
            filterCheck.putIfAbsent(sName, sName);
            if (!sCode.equals(filterClassCode.get(sName))) {
                LOG.info("reloading code " + sName);
                filterRegistry.remove(sName);
            }
        }
        Filter filter = filterRegistry.get(sName);
        if (filter == null) {
            Class clazz = compiler.compile(sCode, sName);
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filter = filterFactory.newInstance(clazz);
            }
        }
        return filter;

    }

    /**
     * @return the total number of filters
     */
    public int filterInstanceMapSize() {
        return filterRegistry.size();
    }


    /**
     * From a file this will read the Filter source code, compile it, and add it to the list of current filters
     * a true response means that it was successful.
     *
     * @param file
     * @return true if the filter in file successfully read, compiled, verified and added to registry
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public boolean putFilter(File file) throws Exception
    {
        try {
            String sName = file.getAbsolutePath();
            if (filterClassLastModified.get(sName) != null && (file.lastModified() != filterClassLastModified.get(sName))) {
                LOG.debug("reloading filter " + sName);
                filterRegistry.remove(sName);
            }
            Filter filter = filterRegistry.get(sName);
            if (filter == null) {
                Class clazz = compiler.compile(file);
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    filter = filterFactory.newInstance(clazz);
                    putFilter(sName, filter, file.lastModified());
                    return true;
                }
            }
        }
        catch (Exception e) {
            LOG.error("Error loading filter! Continuing. file=" + String.valueOf(file), e);
            return false;
        }

        return false;
    }

    void putFilter(String sName, Filter filter, long lastModified)
    {

        String name = filter.filterName();
        filtersByName.put(name, filter);

        filterRegistry.put(sName, filter);
        filterClassLastModified.put(sName, lastModified);
    }

    /**
     * Load and cache filters by className
     *
     * @param classNames The class names to load
     * @return List of the loaded filters
     * @throws Exception If any specified filter fails to load, this will abort. This is a safety mechanism so we can
     * prevent running in a partially loaded state.
     */
    public List<Filter> putFiltersForClasses(String[] classNames) throws Exception
    {
        List<Filter> newFilters = new ArrayList<>();
        for (String className : classNames)
        {
            newFilters.add(putFilterForClassName(className));
        }
        return newFilters;
    }

    public Filter putFilterForClassName(String className) throws Exception
    {
        Class clazz = Class.forName(className);
        if (! Filter.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Specified filter class does not implement Filter interface!");
        }
        else {
            Filter filter = filterFactory.newInstance(clazz);
            putFilter(className, filter, System.currentTimeMillis());
            return filter;
        }
    }

    public Filter getFilterByName(String name)
    {
        if (name == null)
            return null;

        return filtersByName.get(name);
    }

    public Collection<Filter> getAllFilters() {
        return filterRegistry.getAllFilters();
    }

}
