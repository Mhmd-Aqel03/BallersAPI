package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.services.CourtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CourtService courtService;

    @PostMapping("/createCourt")
    public ResponseEntity<Map<String,Object>> createCourt(@RequestBody @Valid Court court) {
        Map<String, Object> response = new HashMap<>();

        courtService.addCourt(court);

        response.put("msg","court successfully created");

        return ResponseEntity.ok(response);
    }

}


