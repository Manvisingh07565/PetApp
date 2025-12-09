package com.petapp.backend.repository;

import com.petapp.backend.entity.VetProfile;
import com.petapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VetRepository extends JpaRepository<VetProfile, Long> {
    Optional<VetProfile> findByUser(User user);
}