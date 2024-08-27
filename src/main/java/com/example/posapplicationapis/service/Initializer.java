package com.example.posapplicationapis.service;

import com.example.posapplicationapis.entities.Role;
import com.example.posapplicationapis.enums.ERole;
import com.example.posapplicationapis.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {


    private RoleRepository roleRepository;

    public Initializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            Role role = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(role);
            roleRepository.save(new Role(ERole.ROLE_CASHIER));
            roleRepository.save(new Role(ERole.ROLE_CHEF));
            roleRepository.save(new Role(ERole.ROLE_CUSTOMER));
        }
    }
}
