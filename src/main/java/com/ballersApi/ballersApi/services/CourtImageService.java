package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.CourtImageDTO;
import com.ballersApi.ballersApi.exceptions.CourtIdNotFoundException;
import com.ballersApi.ballersApi.exceptions.CourtImageIdNotFoundException;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.CourtImage;
import com.ballersApi.ballersApi.repositories.CourtImageRepository;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtImageService {
    private final CourtImageRepository courtImageRepository;
    private final CourtRepository courtRepository;

    public CourtImageService(CourtImageRepository courtImageRepository, CourtRepository courtRepository) {
        this.courtImageRepository = courtImageRepository;
        this.courtRepository = courtRepository;
    }

    public List<CourtImageDTO> getAllImages() {
        List<CourtImage> courtImages =  courtImageRepository.findAll();
        List<CourtImageDTO> courtImageDTOs = courtImages.stream()
                .map(courtImage -> new CourtImageDTO(courtImage.getId(), courtImage.getPhotoUrl(),courtImage.getCourt().getId()))
                .collect(Collectors.toList());

        return courtImageDTOs;
    }

    public List<CourtImage> getImagesByCourtId(Long courtId) {
        if (!courtRepository.existsById(courtId)) {
            throw new CourtIdNotFoundException("Cant get images forz Court with Id " + courtId + " Not Found");
        }
        return courtImageRepository.findByCourtId(courtId);
    }

    public CourtImage getImageById(Long imageId) {
        return courtImageRepository.findById(imageId)
                .orElseThrow(() -> new CourtImageIdNotFoundException("Cant get Court image with Id " + imageId + " not found"));
    }

    public void addImage(Long courtId, CourtImage courtImage) {
        Court court = courtRepository.findById(courtId).orElseThrow(() -> new CourtIdNotFoundException(" Cant add image to Court with Id " + courtId + " Not Found"));

        courtImage.setCourt(court);
        try {
            courtImageRepository.save(courtImage);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("something went wrong while trying to add court image: "  + e.getMessage());
        }
    }

    public CourtImage updateImage(Long imageId, CourtImage updatedImage) {
        CourtImage existingImage = courtImageRepository.findById(imageId).orElseThrow(() -> new CourtImageIdNotFoundException("Cant update image Court image with Id " + imageId + " not found "));
        existingImage.setPhotoUrl(updatedImage.getPhotoUrl());
        return courtImageRepository.save(existingImage);
    }


    public void deleteImage(Long id) {

        if (!courtImageRepository.existsById(id)) {
            throw new CourtImageIdNotFoundException("Cant delete court image with id " + id + " not found");
        }
        try {
            courtImageRepository.deleteById(id);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("something went wrong while trying to delete court image: "  + e.getMessage());
        }
    }
}
