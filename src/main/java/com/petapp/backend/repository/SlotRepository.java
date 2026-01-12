package com.petapp.backend.repository;

import com.petapp.backend.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByVetId(Long vetId);

    List<Slot> findByDateAndVetId(LocalDate date, Long vetId);
}