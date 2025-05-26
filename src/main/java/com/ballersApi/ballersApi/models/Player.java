package com.ballersApi.ballersApi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

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

    // A better solution here is to create an "Endorsement" entity, but this works for now.
    // This is a set of the players that endorsed this player.
    // The @ElementCollection should work like the one to many decorator, without needing to create another model.
    @ElementCollection
    private Set<Long> teamPlayerEndorsements = new HashSet<>();

    @ElementCollection
    private Set<Long> goodLeaderEndorsements = new HashSet<>();

    @ElementCollection
    private Set<Long> positiveAttitudeEndorsements = new HashSet<>();

    @JsonIgnore
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
    private List<SessionTeam> sessionTeams =  new ArrayList<>();


    @JsonIgnore
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
  //  @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "player_favorites",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_id")
    )
    private Set<Player> favorites = new HashSet<>();
    @JsonIgnore
    @JsonBackReference
    @ElementCollection
    private Map<Long, Boolean> pastSessions = new HashMap<>();


}
