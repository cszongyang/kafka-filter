package com.kafkafilter.report;

import com.kafkafilter.configuration.ReporterConfiguration;
import org.stagemonitor.configuration.ConfigurationRegistry;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;


/**
 * @author Zongyang Li
 */
public class MailReporter extends Reporter{


    public MailReporter(ConfigurationRegistry configurationRegistry) {
        super(configurationRegistry);
    }

    @Override
    public void report(Incident incident) {

        final String subject = configurationRegistry.getConfig(ReporterConfiguration.class).getSubject();
        final String from = configurationRegistry.getConfig(ReporterConfiguration.class).getSender();
        final String to = configurationRegistry.getConfig(ReporterConfiguration.class).getRecepient();

        MailRequest mailRequest = new MailRequest(subject, from, to).htmlPart(incident.getDescription()).textPart("test");

        sendMail(mailRequest);

    }

//    private Transport getTransport(Session session) throws MessagingException {
//        Transport transport = session.getTransport(configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpProtocol());
//        transport.connect(configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpHost(),
//                configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpPort(),
//                configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpUser(),
//                configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpPassword());
//        return transport;
//    }

    private void sendMail(MailRequest mailRequest) {
        try {
            Session session = getSession();
            try {
                MimeMessage msg = mailRequest.createMimeMessage(session);
                Transport.send(msg);
            } catch (Exception e) {
                throw new Exception("exception: " + e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Session getSession() {
        int smtpPort = configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpPort();
        final String username = configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpUser();
        final String password = configurationRegistry.getConfig(ReporterConfiguration.class).getSmtpPassword();
        Properties props = new Properties();
        if (isNotEmpty(username) && isNotEmpty(password)) {
            props.put("mail.smtps.auth", "true");
        }
        String host = "smtp.gmail.com";
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return session;
    }

    @Override
    public String getReporterType() {
        return "email";
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

}
