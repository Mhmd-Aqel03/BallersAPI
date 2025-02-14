package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CourtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;
}
