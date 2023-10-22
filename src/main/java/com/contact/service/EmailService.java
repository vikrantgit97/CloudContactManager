package com.contact.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public boolean sendEmail(String subject, String message, String to) {
        boolean flag = false;

        String from = "your-mail";
        String host = "smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();

        //host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        //step 1: to get the session object...
        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-mail", "your-password");
            }

        });

        session.setDebug(true);

        //Step 2: compose the message [text, multimedia]
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            //from email
            mimeMessage.setFrom(from);

            //adding recipient to message
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //adding subject to message
            mimeMessage.setSubject(subject);

            //adding text to message
            //mimeMessage.setText(message);
            mimeMessage.setContent(message, "text/html");

            //send
            //step 3 : send the message using Transport class
            Transport.send(mimeMessage);

            System.out.println("Send Success.......");
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}