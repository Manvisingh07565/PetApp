package com.petapp.backend.repository;

import com.petapp.backend.entity.PetVital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetVitalRepository extends JpaRepository<PetVital, Long> {
    List<PetVital> findByPetIdOrderByRecordDateAsc(Long petId);
}