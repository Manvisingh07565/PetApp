package com.petapp.backend.controller;

import com.petapp.backend.dto.AppointmentRequest;
import com.petapp.backend.entity.Appointment;
import com.petapp.backend.enums.AppointmentStatus;
import com.petapp.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody AppointmentRequest request) {
        try {
            Appointment saved = appointmentService.bookAppointment(request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Appointment>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByOwner(ownerId));
    }

    @GetMapping("/vet/{vetId}")
    public ResponseEntity<List<Appointment>> getByVet(@PathVariable Long vetId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByVet(vetId));
    }
    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        try {
            appointmentService.updateStatus(id, status, null);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/prescriptions/{id}")
    public ResponseEntity<?> savePrescription(@PathVariable Long id, @RequestBody Appointment request) {
        try {
            System.out.println("Backend hit for Appt ID: " + id);

            Appointment updated = appointmentService.savePrescription(id, request.getDiagnosis(), request.getMedicines());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}