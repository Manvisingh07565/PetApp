package com.petapp.backend.scheduler;

import com.petapp.backend.entity.HealthRecord;
import com.petapp.backend.repository.HealthRecordRepository;
import com.petapp.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private EmailService emailService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void runOnStartup() {
        System.out.println(">>> 🚀 Server Started! Checking for pending reminders immediately...");
        checkUpcomingReminders();
    }

    @Scheduled(cron = "0 0 0,9 * * ?")
    @Transactional
    public void checkUpcomingReminders() {
        LocalDate today = LocalDate.now();

        List<HealthRecord> dueRecords = healthRecordRepository.findDueRecordsWithDetails(today);

        if (dueRecords.isEmpty()) {
            System.out.println(">>> No reminders due today (" + today + ")");
            return;
        }

        for (HealthRecord record : dueRecords) {
            String ownerEmail = record.getPet().getOwner().getEmail();
            String petName = record.getPet().getName();
            String vaccine = record.getVaccineName() != null ? record.getVaccineName() : "Checkup";

            System.out.println(">>> 🔔 Sending Alert for: " + petName + " to " + ownerEmail);

            emailService.sendReminderEmail(ownerEmail, petName, vaccine, today.toString());
        }
    }
}