package com.petapp.backend.repository;

import com.petapp.backend.entity.Appointment;
import com.petapp.backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByOwnerId(Long ownerId);

    List<Appointment> findByVetId(Long vetId);
    List<Appointment> findByAppointmentDateAndStatus(LocalDate date, AppointmentStatus status);
    List<Appointment> findByStatus(AppointmentStatus status);

}