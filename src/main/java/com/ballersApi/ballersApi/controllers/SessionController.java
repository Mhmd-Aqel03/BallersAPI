package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Session")
public class SessionController {
    @Autowired
    private SessionService sessionService;
    @GetMapping("getSessionDetails")
    public List<Session> getAllUpcomingSessions(){
        return sessionService.getAllUpcomingSessions();
    }
    @PostMapping("createSession")
    public Session createSession(Session session){
        return sessionService.createSession(session);
    }
    @DeleteMapping("deleteSession")
    public void deleteSession(Session session){
        sessionService.deleteSession(session);
    }


}
