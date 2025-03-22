package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.CourtIdNotFoundException;
import com.ballersApi.ballersApi.exceptions.NoCourtsFoundException;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {
    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<Court> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        if (courts.isEmpty()) {
            throw new NoCourtsFoundException("No courts found ");
        }
        return courts;
    }

    public Court getCourtById(Long id) {

        return courtRepository.findById(id).orElseThrow(() -> new CourtIdNotFoundException("Cant get Court with ID " + id + " not found"));

    }

    public Court addCourt(Court court) {

        return courtRepository.save(court);
    }

    public Court updateCourt(Court court,Long id) {
        Court existingCourt = getCourtById(id);
        existingCourt.setName(court.getName());
        existingCourt.setPlaceId(court.getPlaceId());
        existingCourt.setCity(court.getCity());
        existingCourt.setHasParking(court.isHasParking());
        existingCourt.setHasBathroom(court.isHasBathroom());
        existingCourt.setHasCafeteria(court.isHasCafeteria());
        return courtRepository.save(existingCourt);
    }

    public void deleteCourt(Long id) {
        if (!courtRepository.existsById(id)) {
            throw new CourtIdNotFoundException("Cant Delete Court with ID " + id + " not found");
        }
        courtRepository.deleteById(id);
    }
}
