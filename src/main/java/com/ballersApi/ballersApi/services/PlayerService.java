package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.FavouriteListDTO;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerHistoryDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PlayerService {
    @Autowired
    private final PlayerAuthService playerAuthService;
    @Autowired
    private final PlayerRepository playerRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final SessionRepository sessionRepository;

    public void endorseGoodLeader(long id,Player ourPlayer) {
        checkEndorse(ourPlayer, id, "goodLeader");
        Player player = playerAuthService.getPlayerById(id);

        player.setGoodLeaderCommendations(player.getGoodLeaderCommendations() + 1);
        player.getGoodLeaderEndorsements().add(ourPlayer.getId());

        try {
            playerRepository.save(player);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void endorseTeamPlayer(long id, Player ourPlayer) {
        checkEndorse(ourPlayer, id, "teamPlayer");
        Player player = playerAuthService.getPlayerById(id);

        player.setTeamPlayerCommendations(player.getTeamPlayerCommendations() + 1);
        player.getTeamPlayerEndorsements().add(ourPlayer.getId());

        try {
            playerRepository.save(player);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void endorsePositiveAttitude(long id, Player ourPlayer) {
        checkEndorse(ourPlayer, id, "positiveAttitude");
        Player player = playerAuthService.getPlayerById(id);

        player.setPositiveAttitudeCommendations(player.getPositiveAttitudeCommendations() + 1);
        player.getPositiveAttitudeEndorsements().add(ourPlayer.getId());

        try {
            playerRepository.save(player);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void addFavourite(String playerUsername, long favourite_id) {
        Player ourPlayer = playerAuthService.getPlayerByUsername(playerUsername);
        Player favouritePlayer = userService.getUserByPlayerId(favourite_id).getPlayer();

        ourPlayer.getFavorites().add(favouritePlayer);

        try {
            playerRepository.save(ourPlayer);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while adding player to favourites: " + e.getMessage());
        }
    }

    public List<FavouriteListDTO> getFavourites(String username) {
        Player player = playerAuthService.getPlayerByUsername(username);

        List<Player> favouritePlayers = new ArrayList<>(player.getFavorites());
        List<FavouriteListDTO> favouriteUsers = new ArrayList<>();

        for(Player p: favouritePlayers){
            User user = userService.getUserByPlayerId(p.getId());
            FavouriteListDTO favourite = new FavouriteListDTO();

            favourite.setUsername(user.getUsername());
            favourite.setUserId(user.getId());
            favourite.setPlayerId(p.getId());

            favouriteUsers.add(favourite);
        }

        return favouriteUsers;
    }

    public void removeFavourite(String playerUsername, long favourite_id) {
        Player ourPlayer = playerAuthService.getPlayerByUsername(playerUsername);
        Player favouritePlayer = userService.getUserByPlayerId(favourite_id).getPlayer();
        ourPlayer.getFavorites().remove(favouritePlayer);
        try {
            playerRepository.save(ourPlayer);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while removing player from favourites: " + e.getMessage());
        }
    }

    private void checkEndorse(Player player, long endorserId, String type) {
        switch (type) {
            case "teamPlayer":
                if (player.getTeamPlayerEndorsements().contains(endorserId)) {
                    throw new PlayerAlreadyEndorsedException("player with id: " + endorserId + " already endorsed this player for the team player stat");
                }
                break;

            case "goodLeader":
                if (player.getGoodLeaderEndorsements().contains(endorserId)) {
                    throw new PlayerAlreadyEndorsedException("player with id: " + endorserId + " already endorsed this player for the good leader stat");
                }
                break;

            case "positiveAttitude":
                if (player.getPositiveAttitudeEndorsements().contains(endorserId)) {
                    throw new PlayerAlreadyEndorsedException("player with id: " + endorserId + " already endorsed this player for the positive Attitude stat");
                }
                break;
        }
    }
    @Transactional
    public List<PlayerHistoryDTO> getPlayerSessionHistory(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (player.getPastSessions() == null || player.getPastSessions().isEmpty()) {
            // Return a custom message with the empty result
            throw new PlayerHistoryException("Player has no past session history.");
        }

        List<PlayerHistoryDTO> historyDTOs = new ArrayList<>();
        for (Map.Entry<Long, Boolean> entry : player.getPastSessions().entrySet()) {
            Long sessionId = entry.getKey();
            Boolean won = entry.getValue();

            // Fetch the session using sessionId
            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));


          //  SessionDTO sessionDTO = new SessionDTO(session, userService);
//           String name= userService.getUserByPlayerId(playerId).getUsername();
            String mvpUsername = null;
            if (session.getMvp() != null) {
                mvpUsername = userService.getUserByPlayerId(session.getMvp().getId()).getUsername();
            }


            // Create PlayerHistoryDTO and add to the list
            historyDTOs.add(new PlayerHistoryDTO(session, won, mvpUsername));
        }
        return historyDTOs;
    }


}
