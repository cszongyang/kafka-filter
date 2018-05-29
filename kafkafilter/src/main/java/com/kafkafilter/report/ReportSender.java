package com.kafkafilter.report;

import org.stagemonitor.configuration.ConfigurationRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zongyang Li
 */
public class ReportSender {

    private final List<Reporter> reporters;

    public ReportSender(ConfigurationRegistry configurationRegistry) {
        reporters = new ArrayList<>();
        reporters.add(new MailReporter(configurationRegistry));
    }

    public void send(Incident incident) {
        for (Reporter reporter : reporters) {
            reporter.report(incident);
        }
    }
}
