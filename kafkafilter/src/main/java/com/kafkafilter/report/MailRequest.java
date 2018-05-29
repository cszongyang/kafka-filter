package com.kafkafilter.report;

import javax.mail.*;
import javax.mail.internet.*;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Zongyang Li
 */
public class MailRequest {

    private String subject, from;

    private String recipients;

    private String htmlPart;

    private String textPart;

    /**
     * Creates a new MailRequest
     * @param subject
     *            The email subject
     * @param from
     *            Sender address of the desired mail
     * @param recipients
     *            Multiple recipients for this mail
     */
    public MailRequest(String subject, String from, String recipients) {
        this.subject = subject;
        this.from = from;
        this.recipients = recipients;
    }

    public MailRequest htmlPart(String htmlPart) {
        this.htmlPart = htmlPart;
        return this;
    }

    public MailRequest textPart(String textPart) {
        this.textPart = textPart;
        return this;
    }

    /**
     * Creates a MimeMessage containing given Multipart.
     * Subject, sender and content and session will be set.
     * @param session current mail session
     * @return MimeMessage without recipients
     * @throws MessagingException
     */
    public MimeMessage createMimeMessage(Session session) throws MessagingException {
        if (isEmpty(htmlPart) && isEmpty(textPart)) {
            throw new IllegalArgumentException("Missing email content");
        }
        final MimeMessage msg = new MimeMessage(session);
        msg.setSubject(subject);
        msg.setFrom(new InternetAddress(from));
        msg.setContent(createMultiPart());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients, false));
        return msg;
    }

    /**
     * Creates a Multipart from present parts.
     * @return multipart with all present parts
     * @throws MessagingException
     */
    private Multipart createMultiPart() throws MessagingException {
        Multipart multipart = new MimeMultipart("alternative");
        if (textPart != null) {
            // add text first, to give priority to html
            multipart.addBodyPart((BodyPart) createTextMimePart());
        }
        if (htmlPart != null) {
            multipart.addBodyPart((BodyPart) createHtmlMimePart());
        }
        return multipart;
    }

    /**
     * Creates a MimePart from HTML part.
     *
     * @return mimePart from HTML part
     * @throws MessagingException
     */
    private MimePart createHtmlMimePart() throws MessagingException {
        MimePart bodyPart = new MimeBodyPart();
        bodyPart.setContent(htmlPart, "text/html; charset=utf-8");
        return bodyPart;
    }

    /**
     * Creates a MimePart from text part.
     *
     * @return mimePart from HTML string
     * @throws MessagingException
     */
    private MimePart createTextMimePart() throws MessagingException {
        MimePart bodyPart = new MimeBodyPart();
        bodyPart.setText(textPart);
        return bodyPart;
    }

}
