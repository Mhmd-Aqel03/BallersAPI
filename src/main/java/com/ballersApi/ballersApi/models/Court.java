package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

//  Google Maps an ID for each "location"
    private String placeId;

    private String city;

    private boolean hasParking;

    private boolean hasBathroom;

    private boolean hasCafeteria;
    @OneToMany(mappedBy="court",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourtImage> images;
}
