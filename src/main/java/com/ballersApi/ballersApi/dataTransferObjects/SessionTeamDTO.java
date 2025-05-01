package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Chat;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.services.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SessionTeamDTO {

    private Long id;
    private List<PlayerWithUsernameDTO> players;  // This is a list of PlayerWithUsernameDTO to hold Player and username
    private Team team;

    // Constructor now just takes SessionTeam and Team
    public SessionTeamDTO(SessionTeam team, Team team1, UserService userService) {
        this.id = team.getId();
        this.team = team1;

        if (team.getPlayers() != null) {
            this.players = new ArrayList<>();
            for (Player player : team.getPlayers()) {
                String username = userService.getUserByPlayerId(player.getId()).getUsername();  // Fetch username from UserService
                this.players.add(new PlayerWithUsernameDTO(player, username));  // Create and add PlayerWithUsernameDTO
            }
        } else {
            this.players = new ArrayList<>();
        }
    }

    @Data
    public static class PlayerWithUsernameDTO {
        private Long playerId;

        private String username;

        private String postion;

        private String phoneNumber;

        private String photoUrl;

        private int teamPlayerCommendations = 0;

        private int goodLeaderCommendations = 0;

        private int positiveAttitudeCommendations = 0;

        private int sessionsPlayed = 0;

        private int sessionsWon = 0;

        private int sessionsLost = 0;

        private int MVPs = 0;

        // A better solution here is to create an "Endorsement" entity, but this works for now.
        // This is a set of the players that endorsed this player.
        // The @ElementCollection should work like the one to many decorator, without needing to create another model.
        @ElementCollection
        private Set<Long> teamPlayerEndorsements = new HashSet<>();

        @ElementCollection
        private Set<Long> goodLeaderEndorsements = new HashSet<>();

        @ElementCollection
        private Set<Long> positiveAttitudeEndorsements = new HashSet<>();


        public PlayerWithUsernameDTO(Player player, String username) {
            this.playerId = player.getId();
            this.username = username;
            this.postion= player.getPostion();
            this.phoneNumber = player.getPhoneNumber();
            this.photoUrl = player.getPhotoUrl();
            this.teamPlayerCommendations = player.getTeamPlayerCommendations();
            this.goodLeaderCommendations = player.getGoodLeaderCommendations();
            this.positiveAttitudeCommendations = player.getPositiveAttitudeCommendations();
            this.sessionsPlayed = player.getSessionsPlayed();
            this.sessionsWon = player.getSessionsWon();
            this.sessionsLost = player.getSessionsLost();
            this.MVPs = player.getMVPs();


        }
    }
}
