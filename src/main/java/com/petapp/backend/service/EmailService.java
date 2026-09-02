package com.petapp.backend.service;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    @Value("${resend.from:onboarding@resend.dev}")
    private String fromEmail;

    public EmailService(@Value("${RESEND_API_KEY}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    // =========================
    // OTP EMAIL
    // =========================
    public void sendOtpEmail(String to, String otp, boolean isNewUser) {

        String subject = isNewUser
                ? "Welcome to PawTrack!"
                : "PawTrack Login OTP";

        String body =
                "Hello,\n\n" +
                        "Your OTP for PawTrack is: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes.\n\n" +
                        "Regards,\n" +
                        "PawTrack Team";

        try {

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .text(body)
                    .build();

            SendEmailResponse response = resend.emails().send(request);

            System.out.println("✅ OTP EMAIL SENT SUCCESSFULLY TO: " + to);
            System.out.println("📧 Resend Email ID: " + response.getId());

        } catch (Exception e){

            System.err.println("❌ RESEND EMAIL FAILED: " + e.getMessage());

            throw new RuntimeException(
                    "Failed to send OTP email. Please try again."
            );
        }
    }


    // =========================
    // REMINDER EMAIL
    // =========================
    public void sendReminderEmail(
            String to,
            String petName,
            String vaccineName,
            String dueDate) {

        String body =
                "Hello,\n\n" +
                        "This is a friendly reminder from PawTrack.\n\n" +
                        "Pet: " + petName + "\n" +
                        "Vaccine/Checkup: " + vaccineName + "\n" +
                        "Due Date: " + dueDate + "\n\n" +
                        "Please schedule an appointment with your Vet soon!\n\n" +
                        "Regards,\n" +
                        "PawTrack Team";

        try {

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject("Reminder: " + petName + "'s Vaccination Due!")
                    .text(body)
                    .build();

            SendEmailResponse response = resend.emails().send(request);

            System.out.println("✅ REMINDER SENT TO: " + to);
            System.out.println("📧 Resend Email ID: " + response.getId());

        } catch (Exception e) {

            System.err.println("❌ FAILED TO SEND REMINDER: " + e.getMessage());
        }
    }


    // =========================
    // SIMPLE EMAIL
    // =========================
    public void sendSimpleEmail(
            String to,
            String subject,
            String body) {

        try {

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .text(body)
                    .build();

            SendEmailResponse response = resend.emails().send(request);

            System.out.println("✅ EMAIL SENT TO: " + to);
            System.out.println("📧 Resend Email ID: " + response.getId());

        } catch (Exception e) {

            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
        }
    }


    // =========================
    // HTML EMAIL
    // =========================
    public void sendHtmlEmail(
            String to,
            String subject,
            String htmlContent) {

        try {

            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);

            System.out.println("✅ HTML EMAIL SENT SUCCESSFULLY TO: " + to);
            System.out.println("📧 Resend Email ID: " + response.getId());

        } catch (Exception e) {

            System.err.println("❌ FAILED TO SEND HTML EMAIL: " + e.getMessage());

            throw new RuntimeException("Email sending failed");
        }
    }
}