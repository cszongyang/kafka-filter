package com.kafkafilter.resources;

/**
 * @author Zongyang Li
 */
public class PageView {
    String userID;
    String page;

    public PageView(String userID, String page) {
        this.userID = userID;
        this.page = page;
    }

    public PageView() {
    }

    public String getUserID() {
        return userID;
    }

    public String getPage() {
        return page;
    }
}
