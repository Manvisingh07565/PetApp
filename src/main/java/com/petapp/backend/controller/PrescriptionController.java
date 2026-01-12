package com.petapp.backend.controller;

import com.petapp.backend.entity.Appointment;
import com.petapp.backend.entity.Prescription;
import com.petapp.backend.enums.AppointmentStatus;
import com.petapp.backend.repository.AppointmentRepository;
import com.petapp.backend.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/{appointmentId}")
    public Prescription savePrescription(@PathVariable Long appointmentId, @RequestBody Prescription prescription) {
        Appointment appt = appointmentRepository.findById(appointmentId).orElseThrow();
        prescription.setAppointment(appt);

        appt.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appt);

        return prescriptionRepository.save(prescription);
    }
}