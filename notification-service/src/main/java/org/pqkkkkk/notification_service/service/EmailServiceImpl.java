package org.pqkkkkk.notification_service.service;

import org.pqkkkkk.notification_service.entity.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Override
    public boolean sendSimpleMail(EmailDetails emailDetails) {
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return true;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
            return false;
        }
    }

}
