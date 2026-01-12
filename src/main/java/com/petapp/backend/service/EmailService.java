package com.petapp.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String to, String otp, boolean isNewUser) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("manvirewa2004@gmail.com");
            message.setTo(to);
            message.setSubject(isNewUser ? "Welcome to PawTrack!" : "PawTrack Login OTP");
            message.setText("Hello,\n\nYour OTP for PawTrack is: " + otp + "\n\nThis OTP is valid for 10 minutes.");
            javaMailSender.send(message);
            System.out.println("✅ EMAIL SENT SUCCESSFULLY TO: " + to);
        } catch (Exception e) {
            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
        }
    }

    public void sendReminderEmail(String to, String petName, String vaccineName, String dueDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("manvirewa2004@gmail.com");
            message.setTo(to);
            message.setSubject("🔔 Reminder: " + petName + "'s Vaccination Due!");
            message.setText("Hello,\n\nThis is a friendly reminder from PawTrack.\n\n" +
                    "🐾 Pet: " + petName + "\n" +
                    "💉 Vaccine/Checkup: " + vaccineName + "\n" +
                    "📅 Due Date: " + dueDate + "\n\n" +
                    "Please schedule an appointment with your Vet soon!");
            javaMailSender.send(message);
            System.out.println("✅ REMINDER SENT TO: " + to);
        } catch (Exception e) {
            System.err.println("❌ FAILED TO SEND REMINDER: " + e.getMessage());
        }
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("manvirewa2004@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            System.out.println("✅ EMAIL SENT TO: " + to);
        } catch (Exception e) {
            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("manvirewa2004@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("✅ HTML Email Sent Successfully!");
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send HTML email: " + e.getMessage());
            throw new RuntimeException("Email sending failed");
        }
    }
}