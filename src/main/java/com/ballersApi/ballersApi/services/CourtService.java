package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.CourtIdNotFoundException;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.NoCourtsFoundException;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CourtService {
    private final CourtRepository courtRepository;
    private final SessionRepository sessionRepository;


    public List<Court> getAllCourts() {
        try{
            return courtRepository.findAll();
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to retrieve courts: " + e.getMessage());
        }

    }

    public Court getCourtById(Long id) {
        return courtRepository.findById(id).orElseThrow(() -> new CourtIdNotFoundException("Can't get Court with ID " + id + " not found"));
    }

    @Transactional
    public void addCourt(Court court) {
        try{
            courtRepository.save(court);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to create new court: " + e.getMessage());
        }

    }

    public void updateCourt(Court court,Long id) {
        Court existingCourt = getCourtById(id);

        existingCourt.setName(court.getName());
        existingCourt.setPlaceId(court.getPlaceId());
        existingCourt.setCity(court.getCity());
        existingCourt.setHasParking(court.isHasParking());
        existingCourt.setHasBathroom(court.isHasBathroom());
        existingCourt.setHasCafeteria(court.isHasCafeteria());

        try{
            courtRepository.save(existingCourt);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to create new court: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCourt(Long id) {
        if (!courtRepository.existsById(id)) {
            throw new CourtIdNotFoundException("Cant Delete Court with ID " + id + " not found");
        }
        Court court = getCourtById(id);

        sessionRepository.deleteAllByCourt(court);

        try{
            courtRepository.deleteById(id);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to delete: " + e.getMessage());
        }
    }
}
