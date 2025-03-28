package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.UpdatePlayerDTO;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerAuthService playerAuthService;

    private final PlayerRepository playerRepository;

    private final UserService userService;

    public void endorseGoodLeader(long id){
        Player player = playerAuthService.getPlayerById(id);

        player.setGoodLeaderCommendations(player.getGoodLeaderCommendations() + 1);

        try{
            playerRepository.save(player);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void endorseTeamPlayer(long id){
        Player player = playerAuthService.getPlayerById(id);

        player.setTeamPlayerCommendations(player.getTeamPlayerCommendations() + 1);

        try{
            playerRepository.save(player);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void endorsePositiveAttitude(long id){
        Player player = playerAuthService.getPlayerById(id);

        player.setPositiveAttitudeCommendations(player.getPositiveAttitudeCommendations() + 1);

        try{
            playerRepository.save(player);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error connecting to DB while endorsing player: " + e.getMessage());
        }
    }

    public void addFavourite(String playerUsername, long favourite_id){
        Player ourPlayer = playerAuthService.getPlayerByUsername(playerUsername);
        Player favouritePlayer = playerAuthService.getPlayerById(favourite_id);

        ourPlayer.getFavorites().add(favouritePlayer);

        try{
            playerRepository.save(ourPlayer);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error connecting to DB while adding player to favourites: " + e.getMessage());
        }
    }

    public List<Player> getFavourites(String username){
        Player player =  playerAuthService.getPlayerByUsername(username);

        // Because favourites is a set, we return a list like this
        return new ArrayList<>(player.getFavorites());
    }

    public void removeFavourite(String playerUsername, long favourite_id){
        Player ourPlayer = playerAuthService.getPlayerByUsername(playerUsername);
        Player favouritePlayer = playerAuthService.getPlayerById(favourite_id);

        ourPlayer.getFavorites().remove(favouritePlayer);
        try{
            playerRepository.save(ourPlayer);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error connecting to DB while removing player from favourites: " + e.getMessage());
        }
    }



}
