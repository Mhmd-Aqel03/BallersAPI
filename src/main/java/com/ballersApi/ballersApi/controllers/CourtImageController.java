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

    @GetMapping
    public List<CourtImage> getAllImages() {
        return courtImageService.getAllImages();
    }

    @GetMapping("/court/{courtId}")
    public List<CourtImage> getImagesByCourtId(@PathVariable Long courtId) {
        return courtImageService.getImagesByCourtId(courtId);
    }

    @GetMapping("/{id}")
    public CourtImage getImageById(@PathVariable Long id) {
        return courtImageService.getImageById(id);
    }

    @PostMapping("/add/{courtId}")
    public CourtImage addImage(@PathVariable Long courtId, @RequestBody CourtImage courtImage) {
        return courtImageService.addImage(courtId, courtImage);
    }

    @PutMapping("/update/{imageId}")
    public CourtImage updateImage(@PathVariable Long imageId, @RequestBody CourtImage newPhotoUrl) {
        return courtImageService.updateImage(imageId, newPhotoUrl);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable Long id) {
        courtImageService.deleteImage(id);
    }
}
