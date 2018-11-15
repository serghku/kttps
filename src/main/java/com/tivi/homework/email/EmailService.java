package com.tivi.homework.email;

import com.tivi.homework.model.GameSession;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailService {



    public static void sendMessage(String email, String content, GameSession gameSession) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") +
                    File.separator + "src" +
                    File.separator + "main" +
                    File.separator + "resources" +
                    File.separator + "application.properties");
            Properties properties = new Properties();
            properties.load(reader);
            String gmail = properties.getProperty("gmailUser");
            String password = properties.getProperty("gmailPassword");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(gmail, password);
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("sergei.student1@gmail.com", false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject("KTTT game session : " + gameSession.getId());
            msg.setContent(content, "text/html");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
