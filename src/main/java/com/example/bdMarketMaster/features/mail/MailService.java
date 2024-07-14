package com.example.bdMarketMaster.features.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("pustcpc@gmail.com");

        mailSender.send(message);
    }


    public void sendWelcomeEmail(String toEmail, String userName, String verificationUrl) throws MessagingException {
        String subject = "Welcome to BD Market Master";
        String content = buildWelcomeEmailContent(userName, verificationUrl);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true); // true indicates HTML content

        mailSender.send(message);
    }

    private  String buildWelcomeEmailContent(String userName, String verificationUrl) {
        // Load the HTML template as a string and replace placeholder with actual data
        String htmlTemplate = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><meta name=\"viewport\"" +
                " content=\"width=device-width, initial-scale=1.0\"><style>body {font-family: Arial, " +
                "sans-serif;line-height: 1.6;background-color: #f4f4f4;margin: 0;padding: 0;}.container" +
                " {width: 80%;margin: auto;overflow: hidden;background: #ffffff;padding: 20px;border-radius: " +
                "10px;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}.header {background: #007bff;color: " +
                "#ffffff;padding: 10px 0;text-align: center;}.header h1 {margin: 0;}.content {padding: " +
                "20px;text-align: center;}.content h2 {color: #007bff;}.button {display: inline-block;" +
                "background: #007bff;color: #ffffff;padding: 10px 20px;text-decoration: none;border-radius: " +
                "5px;margin-top: 20px;}.footer {text-align: center;padding: 20px;background: #f4f4f4;color: " +
                "#666666;}.footer a {color: #007bff;text-decoration: none;}</style></head><body><div" +
                " class=\"container\"><div class=\"header\"><h1>Welcome to BD Market Master</h1></div><div" +
                " class=\"content\"><h2>Hello, [User's Name]!</h2><p>Thank you for registering with us." +
                " We are thrilled to have you on board.</p><p>Feel free to explore and let us know if you have " +
                "any questions.</p>  <a href=\"[verifyURL]\" style=\"color: white;\" class=\"button\"><h3>Verify</h3></a></div>" +
                "<div class=\"footer\">" +
                "<p>If you have any questions, contact us at <a href=\"mailto:support@example.com\">support@example.com</a>" +
                ".</p><p>&copy; 2024 Our Service. All rights reserved.</p></div></div></body></html>";
        return htmlTemplate.replace("[User's Name]", userName).replace("[verifyURL]", verificationUrl);
    }
}