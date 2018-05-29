package com.kafkafilter.configuration;

import org.stagemonitor.configuration.ConfigurationOption;
import org.stagemonitor.configuration.ConfigurationOptionProvider;

/**
 * @author Zongyang Li
 */
public class ReporterConfiguration extends ConfigurationOptionProvider {

    private static final String EMAIL_CATEGORY = "email";
    private static final String EMAIL_SENDER = "emailsender";
    private static final String EMAIL_RECIPIENT = "emailrecipient";
    private static final String STMP = "stmp";
    private static final String SUBJECT = "subject";

    private static final String USERNAME = "******"; //change accordingly
    private static final String PASSWORD = "******"; //change accordingly


    private final ConfigurationOption<String> sender = ConfigurationOption.stringOption()
            .key(EMAIL_SENDER)
            .configurationCategory(EMAIL_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("lizongyang@alumni.usc.edu");

    private final ConfigurationOption<String> recepient = ConfigurationOption.stringOption()
            .key(EMAIL_RECIPIENT)
            .configurationCategory(EMAIL_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("zongyanl@usc.edu");

    private final ConfigurationOption<String> smtpProtocol = ConfigurationOption.stringOption()
            .key(STMP)
            .configurationCategory(EMAIL_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("smtps");

    private final ConfigurationOption<String> subject = ConfigurationOption.stringOption()
            .key(SUBJECT)
            .configurationCategory(EMAIL_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("Kafka filter error report");

    public String getSmtpProtocol() {
        return smtpProtocol.get();
    }

    public String getSmtpHost() {
        return "smtp.gmail.com";
    }

    public Integer getSmtpPort() {
        return 587;
    }

    public String getSmtpUser() {
        return USERNAME;
    }

    public String getSmtpPassword() {
        return PASSWORD;
    }

    public String getSender() {
        return sender.get();
    }

    public String getRecepient() {
        return recepient.get();
    }

    public String getSubject() {
        return subject.get();
    }

}
