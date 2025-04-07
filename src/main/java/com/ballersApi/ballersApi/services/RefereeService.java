package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.RefereeDTO;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.Role;
import com.ballersApi.ballersApi.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class RefereeService {

    private UserService userService;

    public User getRefereeById(long id) {
        User user = userService.getUserById(id);

        if(user.getRole() != Role.ROLE_REFEREE){
            throw new UserNotFoundException("Referee with id: " + id + " not found");
        }

        return user;
    }

    public void addReferee(RefereeDTO refereeDTO){
        User referee = new User();

        userService.checkUserInput(refereeDTO.getUsername(), refereeDTO.getPassword(), refereeDTO.getEmail());

        referee.setUsername(refereeDTO.getUsername());
        referee.setPassword(refereeDTO.getPassword());
        referee.setEmail(refereeDTO.getEmail());
        referee.setRole(Role.ROLE_REFEREE);

        userService.addUser(referee);
    }

    public ArrayList<User> getAllReferees(){

        return userService.getUsersByRole(Role.ROLE_REFEREE);

    }

    public void deleteReferee(long id){
        userService.deleteUser(id);
    }
}
