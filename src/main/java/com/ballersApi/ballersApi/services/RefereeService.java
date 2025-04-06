package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.Referee;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.RefereeRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RefereeService {
    private RefereeRepository refereeRepository;

    private UserService userService;

    // Here I get the Referee and not the user the referee is linked to.
    // For auth and stuff we'll need another method.
    public Referee getRefereeById(long id) {
        User user = userService.getUserById(id);

        if(user.getReferee() == null){
            throw new UserNotFoundException("Referee with id: " + id + " not found");
        }

        return user.getReferee();
    }
}
