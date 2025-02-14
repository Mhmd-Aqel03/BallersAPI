package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String postion;

    private String phoneNumber;

    private String photoUrl;

    private int teamPlayerCommendations;

    private int goodLeaderCommendations;

    private int positiveAttitudeCommendations;

    private int sessionsPlayed;

    private int sessionsWon;

    private int sessionsLost;

    private int MVPs;

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
