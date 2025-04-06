package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.CourtImage;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.services.CourtImageService;
import com.ballersApi.ballersApi.services.CourtService;
import com.ballersApi.ballersApi.services.SessionService;
import com.ballersApi.ballersApi.services.SessionTeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CourtService courtService;

    private final CourtImageService courtImageService;

    private final SessionService sessionService;

    private final SessionTeamService sessionTeamService;

    // Courts
    @PostMapping("/createCourt")
    public ResponseEntity<Map<String,Object>> createCourt(@RequestBody @Valid Court court) {
        Map<String, Object> response = new HashMap<>();

        courtService.addCourt(court);

        response.put("msg","court successfully created");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateCourt/{id}")
    public ResponseEntity<Map<String,Object>> updateCourt(@PathVariable Long id,@RequestBody @Valid Court court) {
        Map<String, Object> response = new HashMap<>();

        courtService.updateCourt(court,id);

        response.put("msg","court updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteCourt/{id}")
    public ResponseEntity<Map<String,Object>> deleteCourt(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        courtService.deleteCourt(id);

        response.put("msg", "court deleted successfully");

        return ResponseEntity.ok(response);
    }

    //Court Images
    @PostMapping("/addImage/{courtId}")
    public ResponseEntity<Map<String,Object>> addImage(@PathVariable Long courtId, @RequestBody CourtImage courtImage) {
        Map<String, Object> response = new HashMap<>();

        courtImageService.addImage(courtId, courtImage);

        response.put("msg","court image added successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<Map<String,Object>> deleteImage(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        response.put("msg","court image deleted successfully");

        courtImageService.deleteImage(id);

        return ResponseEntity.ok(response);
    }

    // Sessions
    @PostMapping("/createSession")
    public ResponseEntity<Map<String,Object>> createSession(@Valid @RequestBody Session session){
        Map<String, Object> response = new HashMap<>();

        sessionService.createSession(session);
        sessionTeamService.createTeamSession(session.getId());
        sessionTeamService.createTeamSession(session.getId());

        response.put("msg","Session created successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deleteSession/{id}")
    public ResponseEntity<Map<String,Object>> deleteSession(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        sessionTeamService.deleteAllTeamSessions();
        sessionService.deleteSession(id);

        response.put("msg","Session deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("updateSession/{id}")
    public ResponseEntity<Map<String,Object>> updateSession(@PathVariable Long id,@RequestBody @Valid Session session){
        Map<String, Object> response = new HashMap<>();

        sessionService.updateSession(id, session);

        response.put("msg","Session updated successfully");

        return ResponseEntity.ok(response);
    }
}


