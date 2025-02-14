package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class SessionTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//  Player relation
    @ManyToMany(mappedBy = "sessionTeams")
    private List<Player> players;

//  Session relation
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private Session session;

}
