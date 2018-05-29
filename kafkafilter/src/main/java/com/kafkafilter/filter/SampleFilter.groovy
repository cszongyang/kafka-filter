
package com.kafkafilter.filter;

/**
 * Sample Filter to demonstrate
 * @author Zongyang Li
 */
class SampleFilter implements Filter{



    int filterOrder() {
        return 500
    }

    @Override
    boolean isDisabled() {
        return false
    }

    @Override
    String filterName() {
        return "user id can't be 86. "
    }

    @Override
    boolean shouldFilter(Object msg) {
        if (msg.userID == 86) {
            true
        }
        return false
    }

}