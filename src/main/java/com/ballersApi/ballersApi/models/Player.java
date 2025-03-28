package com.ballersApi.ballersApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Position Can't be empty")
    private String postion;

    @NotBlank(message = "Phone Number Can't be empty")
    private String phoneNumber;

    private String photoUrl;

    private int teamPlayerCommendations = 0;

    private int goodLeaderCommendations = 0;

    private int positiveAttitudeCommendations = 0;

    private int sessionsPlayed = 0;

    private int sessionsWon = 0;

    private int sessionsLost = 0;

    private int MVPs = 0;

    @JsonIgnore
    private String EmailVerificationCode;
    @JsonIgnore
    private String passwordChangeCode;
    @JsonIgnore
    private boolean isVerified = false;
    @JsonIgnore
    private String refreshToken;


    @JsonIgnore
//  Session Team relationship
    @ManyToMany
    @JoinTable(
            name = "player_sessionTeam",
            joinColumns = {
                    @JoinColumn(name = "sessionTeam_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "player_id")
            }
    )
    private List<SessionTeam> sessionTeams;

    @JsonIgnore
//  Player and Chat relationship
    @ManyToMany
    @JoinTable(
            name = "player_chat",
            joinColumns = {
                    @JoinColumn(name = "chat_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "player_id")
            }
    )
    private List<Chat> chats;

    // Player favourite list(We forgor lol)
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "player_favorites",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_id")
    )
    private Set<Player> favorites = new HashSet<>();
}
