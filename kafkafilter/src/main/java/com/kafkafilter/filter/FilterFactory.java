package com.kafkafilter.filter;

/**
 * Interface to provide instances of Filter from a given class.
 * @author Zongyang Li
 */
public interface FilterFactory {

    /**
     * Returns an instance of the specified class.
     *
     * @param clazz the Class to instantiate
     * @return an instance of Filter
     * @throws Exception if an error occurs
     */
    public Filter newInstance(Class clazz) throws Exception;
}
