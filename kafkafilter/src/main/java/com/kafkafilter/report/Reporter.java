package com.kafkafilter.report;

/**
 * @author Zongyang Li
 */

import org.stagemonitor.configuration.ConfigurationRegistry;

/**
 * An Reporter reports incidents to some channel like email.
 */
public abstract class Reporter {

    protected final ConfigurationRegistry configurationRegistry;

    public Reporter(ConfigurationRegistry configurationRegistry) {
        this.configurationRegistry = configurationRegistry;
    }

    /**
     * Triggers an report
     *
     * @param incident
     */
    public abstract void report(Incident incident);

    /**
     * A unique name for this Reporter e.g. email or irc.
     *
     * @return the unique Reporter name
     */
    public abstract String getReporterType();

    /**
     * An Reporter is available if all required configuration options for the particular Reporter are set.
     *
     * @return true, if the Reporter is available, false otherwise
     */
    public abstract boolean isAvailable();

}
