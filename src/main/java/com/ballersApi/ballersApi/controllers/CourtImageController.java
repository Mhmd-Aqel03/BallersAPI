package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.CourtImage;
import com.ballersApi.ballersApi.services.CourtImageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/court-images")
public class CourtImageController {
    private final CourtImageService courtImageService;

    public CourtImageController(CourtImageService courtImageService) {
        this.courtImageService = courtImageService;
    }

    @GetMapping("/court/{courtId}")
    public List<CourtImage> getImagesByCourtId(@PathVariable Long courtId) {
        return courtImageService.getImagesByCourtId(courtId);
    }

    @GetMapping("/{id}")
    public CourtImage getImageById(@PathVariable Long id) {
        return courtImageService.getImageById(id);
    }

}
