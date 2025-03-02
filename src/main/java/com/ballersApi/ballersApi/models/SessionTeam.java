package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    public SessionTeam(Session session ) {

        this.session = session;
    }

 //   @NotNull(message = "Players list cannot be null")
    @Size(min = 3, max = 6, message = "Team must have between 1 and 5 players")
//  Player relation
    @ManyToMany(mappedBy = "sessionTeams")
    private List<Player> players;
    @NotNull(message = "Session cannot be null")
//  Session relation
    @ManyToOne(cascade =  {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "session_id")
    private Session session;


}
