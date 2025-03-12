package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerAuthService playerAuthService;

    private final PlayerRepository playerRepository;

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

}
