package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.PlayerAlreadyEndorsedException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerAuthService playerAuthService;

    private final PlayerRepository playerRepository;

    private final UserService userService;

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
        Player favouritePlayer = playerAuthService.getPlayerById(favourite_id);

        ourPlayer.getFavorites().add(favouritePlayer);

        try {
            playerRepository.save(ourPlayer);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error connecting to DB while adding player to favourites: " + e.getMessage());
        }
    }

    public List<User> getFavourites(String username) {
        Player player = playerAuthService.getPlayerByUsername(username);

        List<User> favouriteUsers = new ArrayList<>();
        for (Player favouritePlayer : player.getFavorites()) {
            favouriteUsers.add(userService.getUserByPlayerId(favouritePlayer.getId()));
        }

        return favouriteUsers;
    }

    public void removeFavourite(String playerUsername, long favourite_id) {
        Player ourPlayer = playerAuthService.getPlayerByUsername(playerUsername);
        Player favouritePlayer = playerAuthService.getPlayerById(favourite_id);

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

}
