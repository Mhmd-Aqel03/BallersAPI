package com.ballersApi.ballersApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor

public class CourtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Photo URL cant be empty")
    @Column(nullable = false)
    private String photoUrl;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "court_id")
    @JsonIgnore
    private Court court;
}
