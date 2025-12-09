package com.petapp.backend.repository;

import com.petapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Email se user dhundne ke liye method
    Optional<User> findByEmail(String email);
}