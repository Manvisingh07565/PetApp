package com.petapp.backend.repository;

import com.petapp.backend.entity.OwnerProfile;
import com.petapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<OwnerProfile, Long> {
    Optional<OwnerProfile> findByUser(User user);
}