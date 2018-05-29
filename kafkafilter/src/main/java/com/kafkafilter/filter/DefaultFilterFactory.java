package com.kafkafilter.filter;

/**
 * Default factory for creating instances of Filter.
 * @author Zongyang Li
 */
public class DefaultFilterFactory implements FilterFactory {

    /**
     * Returns a new implementation of Filter as specified by the provided
     * Class. The Class is instantiated using its nullary constructor.
     *
     * @param clazz the Class to instantiate
     * @return A new instance of Filter
     */
    public Filter newInstance(Class clazz) throws InstantiationException, IllegalAccessException {
        return (Filter) clazz.newInstance();
    }

}