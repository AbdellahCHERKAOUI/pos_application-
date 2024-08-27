package com.example.posapplicationapis.dto.user;

import lombok.Data;

@Data
public class UserAuthDtoResponse {
    private Long id;
    private boolean isAdmin;
    private boolean isCashier;
    private boolean isChef;

}
