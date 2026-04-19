package com.example.backendtemplate.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailUtil {

    @Value("${spring.mail.username}")      // 从 application.yml 读取
    private String fromEmail;

    @Value("${spring.mail.password}")      // 授权码
    private String emailPassword;

    public void sendEmail(String toEmail, String subject, String body) {
        String host = "smtp.qq.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 建议加上，避免协议问题

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("邮件发送成功: " + toEmail);
        } catch (Exception e) {
            System.err.println("邮件发送失败: " + e.getMessage());
        }
    }
}