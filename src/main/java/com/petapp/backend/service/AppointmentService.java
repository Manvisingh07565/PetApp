package com.petapp.backend.service;

import com.petapp.backend.dto.AppointmentRequest;
import com.petapp.backend.entity.Appointment;
import com.petapp.backend.entity.Slot;
import com.petapp.backend.entity.User;
import com.petapp.backend.enums.AppointmentStatus;
import com.petapp.backend.repository.AppointmentRepository;
import com.petapp.backend.repository.SlotRepository;
import com.petapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.petapp.backend.repository.PrescriptionRepository;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {
        // 1. Check Slot
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // 2. Capacity Check
        if (slot.getBookedCount() >= slot.getCapacity()) {
            throw new RuntimeException("Slots full for this date. Please try another.");
        }

        // 3. Fetch Owner & Vet
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        User vet = userRepository.findById(request.getVetId())
                .orElseThrow(() -> new RuntimeException("Vet not found"));

        // 4. Create Appointment Object
        Appointment appt = new Appointment();
        appt.setOwner(owner);
        appt.setVet(vet);
        appt.setSlot(slot);
        appt.setAppointmentDate(request.getAppointmentDate());
        appt.setPetName(request.getPetName());
        appt.setReason(request.getReason());
        appt.setType(request.getType());
        appt.setStatus(AppointmentStatus.PENDING);

        // 5. Increment Slot Booked Count
        slot.setBookedCount(slot.getBookedCount() + 1);
        slotRepository.save(slot);

        return appointmentRepository.save(appt);
    }

    public List<Appointment> getAppointmentsByOwner(Long ownerId) {
        return appointmentRepository.findByOwnerId(ownerId);
    }

    public List<Appointment> getAppointmentsByVet(Long vetId) {
        return appointmentRepository.findByVetId(vetId);
    }

    @Transactional
    public Appointment updateStatus(Long id, AppointmentStatus status, String meetingLink) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setStatus(status);
        if (meetingLink != null) {
            appt.setMeetingLink(meetingLink);
        }

        if (status == AppointmentStatus.CANCELLED) {
            Slot slot = appt.getSlot();
            if (slot != null) {
                slot.setBookedCount(slot.getBookedCount() - 1);
                slotRepository.save(slot);
            }
        }

        try {
            String ownerEmail = appt.getOwner().getEmail();
            String petName = appt.getPetName();
            String date = appt.getAppointmentDate().toString();

            if (status == AppointmentStatus.CONFIRMED) {
                String subject = "Booking Confirmed: " + petName;
                String body = "Hello,\n\nYour appointment for " + petName + " on " + date + " is CONFIRMED.\n" +
                        "Type: " + appt.getType() + "\n" +
                        (meetingLink != null ? "Meeting Link: " + meetingLink : "");

                emailService.sendSimpleEmail(ownerEmail, subject, body);
                System.out.println("✅ Confirmation Mail Sent to: " + ownerEmail);
            }
            else if (status == AppointmentStatus.CANCELLED) {
                String subject = "Appointment Cancelled";
                String body = "Hello,\n\nYour appointment for " + petName + " on " + date + " has been CANCELLED.";

                emailService.sendSimpleEmail(ownerEmail, subject, body);
                System.out.println("✅ Cancellation Mail Sent to: " + ownerEmail);
            }
        } catch (Exception e) {
            System.err.println("❌ Mail Trigger Failed in updateStatus: " + e.getMessage());
        }

        return appointmentRepository.save(appt);
    }

    public String generateMeetingLink(Long appointmentId) {
        return "https://meet.google.com/abc-defg-hij-" + appointmentId + "-" + System.currentTimeMillis();
    }

    @Transactional
    public Appointment confirmTeleconsult(Long id) {
        Appointment appt = appointmentRepository.findById(id).orElseThrow();

        String link = generateMeetingLink(id);
        appt.setMeetingLink(link);
        appt.setStatus(AppointmentStatus.CONFIRMED);

        String htmlMessage = "<h2>Teleconsultation Confirmed!</h2>" +
                "<p>Click the button below to join:</p>" +
                "<a href='" + link + "' style='background:blue;color:white;padding:10px;text-decoration:none;'>JOIN MEETING NOW</a>";

        emailService.sendHtmlEmail(appt.getOwner().getEmail(), "Your Teleconsult Link", htmlMessage);

        return appointmentRepository.save(appt);
    }
    @Transactional
    public Appointment savePrescription(Long id, String diagnosis, String medicines) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setDiagnosis(diagnosis);
        appt.setMedicines(medicines);
        appt.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appt);
    }
}