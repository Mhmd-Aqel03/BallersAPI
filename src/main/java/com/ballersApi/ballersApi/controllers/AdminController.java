package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.AdminSessionDTO;
import com.ballersApi.ballersApi.dataTransferObjects.RefereeDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.CourtImage;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.CourtImageService;
import com.ballersApi.ballersApi.services.CourtService;
import com.ballersApi.ballersApi.services.RefereeService;
import com.ballersApi.ballersApi.services.SessionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5000",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CourtService courtService;

    private final CourtImageService courtImageService;

    private final SessionService sessionService;

    private final RefereeService refereeService;

    // Courts

    @GetMapping("/getAllCourts")
    public ResponseEntity<Map<String, Object>> getAllCourts() {
        Map<String, Object> response = new HashMap<>();

        response.put("courts", courtService.getAllCourts());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCourt/{id}")
    public ResponseEntity<Map<String, Object>> getCourtById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        response.put("court", courtService.getCourtById(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createCourt")
    public ResponseEntity<Map<String, Object>> createCourt(@RequestBody @Valid Court court) {
        Map<String, Object> response = new HashMap<>();

        courtService.addCourt(court);

        response.put("msg", "court successfully created");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateCourt/{id}")
    public ResponseEntity<Map<String, Object>> updateCourt(@PathVariable Long id, @RequestBody @Valid Court court) {
        Map<String, Object> response = new HashMap<>();

        courtService.updateCourt(court, id);

        response.put("msg", "court updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteCourt/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourt(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        courtService.deleteCourt(id);

        response.put("msg", "court deleted successfully");

        return ResponseEntity.ok(response);
    }

    //Court Images
    @GetMapping("/getAllCourtImages")
    public ResponseEntity<Map<String, Object>> getAllImages() {
        Map<String, Object> response = new HashMap<>();

        response.put("courtImages", courtImageService.getAllImages());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createCourtImage/{courtId}")
    public ResponseEntity<Map<String, Object>> addImage(@PathVariable Long courtId, @RequestBody CourtImage courtImage) {
        Map<String, Object> response = new HashMap<>();

        courtImageService.addImage(courtId, courtImage);

        response.put("msg", "court image added successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteCourtImage/{id}")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        response.put("msg", "court image deleted successfully");

        courtImageService.deleteImage(id);

        return ResponseEntity.ok(response);
    }

    // Sessions
    @GetMapping("/getAllSessions")
    public ResponseEntity<Map<String, Object>> getAllUpcomingSessions() {
        Map<String, Object> response = new HashMap<>();

        response.put("sessions", sessionService.getAllSessions());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getSession/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable long id) {
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/createSession")
    public ResponseEntity<Map<String, Object>> createSession(@Valid @RequestBody AdminSessionDTO adminSessionDTO) {
        Map<String, Object> response = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        Session session = new Session();
        session.setType(adminSessionDTO.getType());
        session.setMatchDate(adminSessionDTO.getMatchDate());
        session.setMatchStartTime(LocalTime.parse(adminSessionDTO.getMatchStartTime(), formatter));
        session.setMatchEndTime(LocalTime.parse(adminSessionDTO.getMatchEndTime(), formatter));
        session.setMaxPlayers(adminSessionDTO.getMaxPlayers());
        session.setPrice(adminSessionDTO.getPrice());
        session.setPlayerCount(0);

        sessionService.createSession(session);

        response.put("msg", "Session created successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteSession/{id}")
    public ResponseEntity<Map<String, Object>> deleteSession(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        sessionService.deleteSession(id);

        response.put("msg", "Session deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateSession/{id}")
    public ResponseEntity<Map<String, Object>> updateSession(@PathVariable Long id, @RequestBody @Valid AdminSessionDTO adminSessionDTO) {
        Map<String, Object> response = new HashMap<>();

        sessionService.updateSession(id, adminSessionDTO);

        response.put("msg", "Session updated successfully");

        return ResponseEntity.ok(response);
    }

    // Referee
    @PostMapping("/createReferee")
    public ResponseEntity<Map<String, Object>> createReferee(@Valid @RequestBody RefereeDTO refereeDTO) {
        Map<String, Object> response = new HashMap<>();

        refereeService.addReferee(refereeDTO);

        response.put("msg", "referee created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllReferees")
    public ResponseEntity<Map<String, Object>> getAllReferees() {
        Map<String, Object> response = new HashMap<>();

        ArrayList<User> referee = refereeService.getAllReferees();
        response.put("referees", referee);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteReferee/{id}")
    public ResponseEntity<Map<String, Object>> deleteReferee(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        refereeService.deleteReferee(id);
        response.put("msg", "referee deleted successfully");

        return ResponseEntity.ok(response);
    }
}
