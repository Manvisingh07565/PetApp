package com.petapp.backend.repository;

import com.petapp.backend.entity.Pet;
import com.petapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);
}