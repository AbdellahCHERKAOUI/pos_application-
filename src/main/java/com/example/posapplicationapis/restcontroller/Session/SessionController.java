package com.example.posapplicationapis.restcontroller.Session;

import com.example.posapplicationapis.dto.password.UpdatePasswordDto;
import com.example.posapplicationapis.dto.session.SessionDtoRequest;
import com.example.posapplicationapis.dto.session.SessionDtoResponse;
import com.example.posapplicationapis.services.session.SessionService;
import com.example.posapplicationapis.services.session.SessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController {
    @Autowired
    private SessionServiceImpl sessionService;

    @PostMapping("/add-session")
    public ResponseEntity<SessionDtoResponse> createSession(@RequestBody SessionDtoRequest sessionDtoRequest) {
        SessionDtoResponse createdSession = sessionService.createSession(sessionDtoRequest);
        return ResponseEntity.ok(createdSession);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDtoResponse> getSessionById(@PathVariable Long id) {
        SessionDtoResponse session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    @GetMapping
    public ResponseEntity<List<SessionDtoResponse>> getAllSessions() {
        List<SessionDtoResponse> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDtoResponse> updateSession(@PathVariable Long id, @RequestBody SessionDtoRequest sessionDtoRequest) {
        SessionDtoResponse updatedSession = sessionService.updateSession(id, sessionDtoRequest);
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<Void> updateSessionPassword(@PathVariable Long id, @RequestBody UpdatePasswordDto updatePasswordDto) {
        sessionService.updatePassword(id,updatePasswordDto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

