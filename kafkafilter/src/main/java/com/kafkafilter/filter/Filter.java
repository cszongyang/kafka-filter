package com.kafkafilter.filter;

/**
 * @author Zongyang Li
 */
public interface Filter {

    boolean isDisabled();

    String filterName();

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    boolean shouldFilter(Object msg);

//    O apply(I input);
}
