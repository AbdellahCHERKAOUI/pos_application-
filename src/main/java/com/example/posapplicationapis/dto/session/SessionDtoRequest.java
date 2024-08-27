package com.example.posapplicationapis.dto.session;

import com.example.posapplicationapis.dto.menu.MenuDtoResponse;
import com.example.posapplicationapis.enums.SessionStatus;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionDtoRequest {
    private Long userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
/*
    private String password;
*/
    @Enumerated
    private SessionStatus sessionStatus;
    private List<Long> categoryIds;
}
