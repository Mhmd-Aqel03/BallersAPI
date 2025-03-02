package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Court name cannot be empty")
    @Size(min = 3, max = 100, message = "Court name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    //  Google Maps an ID for each "location"
    @NotNull(message = "Place ID cannot be null")
    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    @NotBlank(message = "City name cannot be empty")
    private String city;

    private boolean hasParking;

    private boolean hasBathroom;

    private boolean hasCafeteria;

    //@OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    //@ToString.Exclude
    //@Builder.Default
    //private List<CourtImage> images = new ArrayList<>();
}
