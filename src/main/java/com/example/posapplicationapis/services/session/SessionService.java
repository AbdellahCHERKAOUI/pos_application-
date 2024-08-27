package com.example.posapplicationapis.services.session;

import com.example.posapplicationapis.dto.session.SessionDtoRequest;
import com.example.posapplicationapis.dto.session.SessionDtoResponse;

import java.util.List;

public interface SessionService {
    SessionDtoResponse createSession(SessionDtoRequest sessionDtoRequest);

    SessionDtoResponse getSessionById(Long id);

    List<SessionDtoResponse> getAllSessions();

    SessionDtoResponse updateSession(Long id, SessionDtoRequest sessionDtoRequest);

    void deleteSession(Long id);
    void updatePassword(Long id, String newPassword);

    SessionDtoResponse getSessionByUserId(Long userId);
}
