package com.example.posapplicationapis.dto.user;

import com.example.posapplicationapis.entities.Role;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserDtoRequest {
    private String name;
    private String password;

    private String city;

    private String address;

    private String country;

    private String phoneNumber;

    private String postalCode;

    private Boolean active;

    private Set<String> roles = new HashSet<>();
}
