package com.petapp.backend.repository;

import com.petapp.backend.entity.HealthRecord;
import com.petapp.backend.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    // Standard lookup
    List<HealthRecord> findByPet(Pet pet);


    @Query("SELECT h FROM HealthRecord h JOIN FETCH h.pet p JOIN FETCH p.owner WHERE h.nextDueDate = :date")
    List<HealthRecord> findDueRecordsWithDetails(@Param("date") LocalDate date);

    // For Dashboard Stats
    @Query("SELECT h FROM HealthRecord h WHERE h.pet.owner.email = :email AND h.nextDueDate >= CURRENT_DATE ORDER BY h.nextDueDate ASC")
    List<HealthRecord> findUpcomingReminders(@Param("email") String email);
}