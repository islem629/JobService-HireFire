package com.example.jobservice.service;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private static final String DEFAULT_FROM = "hirefire.platform@gmail.com";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendJobApplication(String companyEmail,
                                   String candidateEmail,
                                   String subject,
                                   String body,
                                   File cvFile) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            boolean multipart = (cvFile != null);
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "UTF-8");

            helper.setFrom(DEFAULT_FROM);
            helper.setTo(companyEmail);
            helper.setSubject(subject);
            helper.setText(body, false);


            if (candidateEmail != null && !candidateEmail.isBlank()) {
                helper.setReplyTo(candidateEmail);
            }

            if (cvFile != null) {
                FileSystemResource fileResource = new FileSystemResource(cvFile);
                helper.addAttachment("CV.pdf", fileResource);
            }

            mailSender.send(message);
            System.out.println("[DEBUG] Job application email sent to " + companyEmail);

        } catch (MessagingException e) {
            System.out.println("[ERROR] Failed to send job application email: " + e.getMessage());
            throw new RuntimeException("Failed to send application email", e);
        }
    }
}
