package com.ballersApi.ballersApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor

public class CourtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Photo URL cant be null")
    @Column(nullable = false)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id",nullable = false)
    @NotNull
    //if you want to return only the image without assoicated court un comment jsonignore
    @JsonIgnore
   // @ToString.Exclude
    private Court court;
}
