package com.project.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public boolean sendEmail(String subject, String message, String to) {
        boolean flag = false;

        String from = "examplefrom@gmail.com";

        String host = "smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        //step 1: to get the session object...
        Session session = Session.getInstance(properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "example-userename@gmail.com", "example-password");
                    }

                });

        session.setDebug(true);

        //Step 2: compose the message [text, multimedia]
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            //from email
            mimeMessage.setFrom(from);

            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            mimeMessage.setSubject(subject);

            // mimeMessage.setText(message);
            mimeMessage.setContent(message, "text/html");

            //step 3 : send the message using Transport class
            Transport.send(mimeMessage);

            flag = true;

        } catch (Exception e) {
            e.getMessage();
        }
        return flag;
    }

}
