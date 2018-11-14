package com.tivi.homework.email;

import com.tivi.homework.model.GameSession;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailService {



    public static void sendMessage(String email, String content, GameSession gameSession) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sergei.student1@gmail.com", "serghteststudent");
            }
        });
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress("sergei.student1@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("KTTPS game session : "+gameSession.getId());
        msg.setContent(content, "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }
}
