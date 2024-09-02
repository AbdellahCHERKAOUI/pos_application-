package com.example.posapplicationapis.service;

import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.entities.Role;
import com.example.posapplicationapis.enums.ERole;
import com.example.posapplicationapis.enums.IngredientUnitType;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {


    private RoleRepository roleRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    public Initializer(RoleRepository roleRepository, IngredientRepository ingredientRepository) {

        this.roleRepository = roleRepository;
        this.ingredientRepository= ingredientRepository;
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
