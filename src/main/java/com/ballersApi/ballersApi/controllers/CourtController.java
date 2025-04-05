package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.services.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/court")
public class CourtController {
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping("/getAllCourts")
    public ResponseEntity<Map<String,Object>> getAllCourts() {
        Map<String, Object> response = new HashMap<>();

        response.put("courts", courtService.getAllCourts());

        return ResponseEntity.ok(response);
    }

    @GetMapping("getCourt/{id}")
    public ResponseEntity<Map<String,Object>> getCourtById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        response.put("court", courtService.getCourtById(id));

        return ResponseEntity.ok(response);
    }



    @PutMapping("/update/{id}")
    public Court updateCourt(@PathVariable Long id,@RequestBody @Valid Court court) {
        return courtService.updateCourt(court,id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCourt(@PathVariable Long id) {
        courtService.deleteCourt(id);
    }
}

