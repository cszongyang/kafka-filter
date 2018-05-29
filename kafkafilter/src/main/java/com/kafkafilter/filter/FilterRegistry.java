package com.kafkafilter.filter;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zongyang Li
 */
public class FilterRegistry
{
    private final ConcurrentHashMap<String, Filter> filters = new ConcurrentHashMap<String, Filter>();

    public Filter remove(String key) {
        return this.filters.remove(key);
    }

    public Filter get(String key) {
        return this.filters.get(key);
    }

    public void put(String key, Filter filter) {
        this.filters.putIfAbsent(key, filter);
    }

    public int size() {
        return this.filters.size();
    }

    public Collection<Filter> getAllFilters() {
        return this.filters.values();
    }
}