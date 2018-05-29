package com.kafkafilter.report;

/**
 * @author Zongyang Li
 */
public class Incident {

    private String firstFailureAt;
    private String checkName;

    public Incident(String description) {
        this.description = description;
    }

    public String getFirstFailureAt() {
        return firstFailureAt;
    }

    public void setFirstFailureAt(String firstFailureAt) {
        this.firstFailureAt = firstFailureAt;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

}
