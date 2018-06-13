package com.kafkafilter;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author Zongyang Li
 */
public class Application {

    public static void main(String[] args) {
        KafkaFilter kf = new KafkaFilter();
        kf.init();
    }
}
