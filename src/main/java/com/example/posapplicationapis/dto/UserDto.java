package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.entities.Role;

import java.util.HashSet;
import java.util.Set;

public class UserDto {


    private Long id;

    private String name;

    private String city;

    private String address;

    private String country;

    private String phoneNumber;

    private String postalCode;

    private String password;

    private String image;

    private Boolean active = true;

    private Set<Role> roles = new HashSet<>();


    // Getters and Setters
}

