package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.dto.user.UserDtoResponse;
import com.example.posapplicationapis.enums.SessionStatus;
import jakarta.persistence.*;


import java.time.LocalDateTime;


public class SessionDto {

    private Long id;
    private UserDtoResponse userDtoResponse;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private String password;
    @Enumerated
    private SessionStatus sessionStatus;
     private MenuDto menuDto;

}

