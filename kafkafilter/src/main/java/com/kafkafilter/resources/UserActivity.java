package com.kafkafilter.resources;

/**
 * @author Zongyang Li
 */
public class UserActivity {
    String userId;
    String userName;
    String zipcode;
    String[] interests;
    String searchTerm;
    String page;

    public UserActivity(String userId, String userName, String zipcode, String[] interests, String searchTerm, String page) {
        this.userId = userId;
        this.userName = userName;
        this.zipcode = zipcode;
        this.interests = interests;
        this.searchTerm = searchTerm;
        this.page = page;
    }
    public UserActivity() {
    }

    public UserActivity updateSearch(String searchTerm) {
        this.searchTerm = searchTerm;
        return this;
    }
}
