package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Player")
public class PlayerController {
   @Autowired
    private PlayerService playerService;

   @GetMapping("getAllPlayers")
    public ResponseEntity<List<Player>> getAllPlayers() {
       return ResponseEntity.status(HttpStatus.OK).body(playerService.getAllPlayers());
   }
   @GetMapping("getPlayerById/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
       return playerService.getPlayerById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
   }




}
