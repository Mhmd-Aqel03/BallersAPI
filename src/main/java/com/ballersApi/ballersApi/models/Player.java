package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
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

    private String EmailVerificationCode;

    private String passwordChangeCode;

    private boolean isVerified = false;

    private String refreshToken;


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
}
