package com.petapp.backend.scheduler;

import com.petapp.backend.entity.Appointment;
import com.petapp.backend.enums.AppointmentStatus;
import com.petapp.backend.repository.AppointmentRepository;
import com.petapp.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AppointmentReminderScheduler {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;

    // A. SERVER START HOTE HI CHALEGA
    @EventListener(ApplicationReadyEvent.class)
    public void onServerStart() {
        System.out.println("🚀 Server Started: Checking for immediate reminders...");
        sendReminders();
    }

    // B. HAR 30 MINUTE MEIN AUTOMATIC CHALEGA
    @Scheduled(cron = "0 0/30 * * * *")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> appts = appointmentRepository.findByStatus(AppointmentStatus.CONFIRMED);

        for (Appointment appt : appts) {
            LocalDateTime apptDateTime = LocalDateTime.of(appt.getAppointmentDate(), appt.getSlot().getStartTime());

            long hoursLeft = java.time.Duration.between(now, apptDateTime).toHours();
            long minutesLeft = java.time.Duration.between(now, apptDateTime).toMinutes();

            // 24 Hour Reminder
            if (hoursLeft == 24) {
                sendMail(appt, "24-Hour Reminder");
            }
            else if (minutesLeft >= 50 && minutesLeft <= 70) {
                sendMail(appt, "1-Hour Reminder");
            }
        }
    }

    private void sendMail(Appointment appt, String type) {
        String body = "Hello, this is a " + type + " for your appointment of " + appt.getPetName();
        emailService.sendSimpleEmail(appt.getOwner().getEmail(), "Appointment Reminder", body);
        System.out.println("✅ " + type + " sent to: " + appt.getOwner().getEmail());
    }
}
