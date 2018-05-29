package com.kafkafilter.resources;

/**
 * @author Zongyang Li
 */
public class Search {
    String userID;
    String searchTerms;

    public Search(String userID, String searchTerms) {
        this.userID = userID;
        this.searchTerms = searchTerms;
    }

    public Search() {
    }

    public String getUserID() {
        return userID;
    }

    public String getSearchTerms() {
        return searchTerms;
    }
}
