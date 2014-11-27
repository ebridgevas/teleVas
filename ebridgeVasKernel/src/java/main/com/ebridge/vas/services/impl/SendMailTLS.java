package com.ebridge.vas.services.impl;

import org.apache.commons.mail.EmailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailTLS {

    public void send(String[] destinations, String subject, String payload) throws EmailException {

        final String username = "telecelvas@gmail.com";
        final String password = "changeit2014";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("telecelvas@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinations[0]));
            message.setSubject(subject);
            message.setContent(payload, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailException(e);
        }
    }

//    public static void main(String[] args) throws EmailException {
//        String payload = new ActivationMessageFactory().createPayload();
//        new SendMailTLS().send(new String[]{"david@ebridgevas.com"},"Test", payload);
//    }
}