package com.petapp.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Naya parameter 'isNewUser' add kiya
    public void sendOtpEmail(String toEmail, String otp, boolean isNewUser) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Apna asli email yahan likhein
        message.setFrom("apna.real.email@gmail.com");
        message.setTo(toEmail);

        if (isNewUser) {
            // Agar Register kar raha hai
            message.setSubject("Welcome to PetApp - Registration OTP");
            message.setText("Hello,\n\nWelcome to PetApp! 🐾\n\nYour OTP for account registration is: " + otp + "\n\nThis OTP is valid for 10 minutes.");
        } else {
            // Agar Login kar raha hai
            message.setSubject("PetApp Login OTP");
            message.setText("Hello,\n\nWelcome Back! \n\nYour OTP for login is: " + otp + "\n\nThis OTP is valid for 10 minutes.");
        }

        mailSender.send(message);
        System.out.println("Mail sent successfully to " + toEmail);
    }
}