package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.CourtIdNotFoundException;
import com.ballersApi.ballersApi.exceptions.CourtImageIdNotFoundException;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.CourtImage;
import com.ballersApi.ballersApi.repositories.CourtImageRepository;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtImageService {
    private final CourtImageRepository courtImageRepository;
    private final CourtRepository courtRepository;

    public CourtImageService(CourtImageRepository courtImageRepository, CourtRepository courtRepository) {
        this.courtImageRepository = courtImageRepository;
        this.courtRepository = courtRepository;
    }

    public List<CourtImage> getAllImages() {
        return courtImageRepository.findAll();
    }

    public List<CourtImage> getImagesByCourtId(Long courtId) {
        if (!courtRepository.existsById(courtId)) {
            throw new CourtIdNotFoundException("Court with Id " + courtId + " Not Found");
        }
        return courtImageRepository.findByCourtId(courtId);
    }

    public CourtImage addImage(Long courtId, CourtImage courtImage) {
        Court court = courtRepository.findById(courtId).orElseThrow(() -> new CourtIdNotFoundException("Court with Id " + courtId + " Not Found"));

        courtImage.setCourt(court);

        return courtImageRepository.save(courtImage);
    }

    public CourtImage updateImage(Long imageId,String newPhotoUrl){
        CourtImage existingImage = courtImageRepository.findById(imageId).orElseThrow(()-> new CourtImageIdNotFoundException("Court image with Id " + imageId +" not found "));
        existingImage.setPhotoUrl(newPhotoUrl);
        return courtImageRepository.save(existingImage);
    }


    public void deleteImage(Long id) {

        if (!courtImageRepository.existsById(id)) {
            throw new CourtImageIdNotFoundException("Cant delete court image with id " + id + " not found");
        }
        courtImageRepository.deleteById(id);
    }
}
