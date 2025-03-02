package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.services.CourtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @GetMapping("/{id}")
    public Court getCourtById(@PathVariable("id") Long id) {
        return courtService.getCourtById(id);
    }

    @PostMapping("/create")
    public Court createCourt(@RequestBody @Valid Court court) {
        return courtService.addCourt(court);
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

