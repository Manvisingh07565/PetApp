package com.petapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;      // Dog, Cat, etc.
    private String breed;
    private String gender;       // Male, Female
    private String dateOfBirth;  // YYYY-MM-DD
    private String microchipId;
    private String photoUrl;     // Pet ki photo

    @Column(length = 500)
    private String notes;        // Milestone 2 Requirement

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private User owner;
}