package com.example.posapplicationapis.entities;

import com.example.posapplicationapis.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private String password;
    @Enumerated
    private SessionStatus sessionStatus;
    @OneToOne
    private Menu menu;

    // Getters and Setters
}

