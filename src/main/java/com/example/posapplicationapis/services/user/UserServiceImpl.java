package com.example.posapplicationapis.services.user;

import com.example.posapplicationapis.dto.user.UserDtoRequest;
import com.example.posapplicationapis.dto.user.UserDtoResponse;
import com.example.posapplicationapis.entities.Image;
import com.example.posapplicationapis.entities.Role;
import com.example.posapplicationapis.entities.User;
import com.example.posapplicationapis.enums.ERole;
import com.example.posapplicationapis.repositories.ImageRepository;
import com.example.posapplicationapis.repositories.RoleRepository;
import com.example.posapplicationapis.repositories.UserRepository;
import com.example.posapplicationapis.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;


    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest signUpRequest) {
            Set<String> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();


            if (strRoles == null || strRoles.isEmpty()) {
                Role defaultRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(defaultRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role.toLowerCase()) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "chef":
                            Role chefRole = roleRepository.findByName(ERole.ROLE_CHEF)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(chefRole);
                            break;
                        case "cashier":
                            Role cashierRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(cashierRole);
                            break;
                        default:
                            Role defaultRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(defaultRole);
                    }
                });

            User user = new User();
            user.setName(signUpRequest.getName());
            user.setCity(signUpRequest.getCity());
            user.setAddress(signUpRequest.getAddress());
            user.setCountry(signUpRequest.getCountry());
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
            user.setPostalCode(signUpRequest.getPostalCode());
//            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//            user.setImage(signUpRequest.getImage());
            user.setActive(true); // Assuming new users are active by default
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            return convertToDto(savedUser);
        }

        return convertToDto(null);
    }

    @Override
    @Transactional
    public UserDtoResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Override
    @Transactional
    public List<UserDtoResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDtoResponse updateUser(Long id, UserDtoRequest userDtoRequest) {
        Set<String> strRoles = userDtoRequest.getRoles();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null || strRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(defaultRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "chef":
                        Role chefRole = roleRepository.findByName(ERole.ROLE_CHEF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(chefRole);
                        break;
                    case "cashier":
                        Role cashierRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(cashierRole);
                        break;
                    default:
                        Role defaultRole = roleRepository.findByName(ERole.ROLE_CASHIER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(defaultRole);
                }
            });

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            user.setName(userDtoRequest.getName());
            user.setCity(userDtoRequest.getCity());
            user.setAddress(userDtoRequest.getAddress());
            user.setCountry(userDtoRequest.getCountry());
            user.setPhoneNumber(userDtoRequest.getPhoneNumber());
            user.setPostalCode(userDtoRequest.getPostalCode());
//        user.setPassword(passwordEncoder.encode(userDtoRequest.getPassword())); // Hashing password
//        user.setImage(userDtoRequest.getImage());
            user.setActive(userDtoRequest.getActive());
        user.setRoles(roles);

            User updatedUser = userRepository.save(user);
            return convertToDto(updatedUser);
        }
        return convertToDto(null);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UserDtoResponse> getUsersByRole(String role) {
        ERole eRole = ERole.valueOf("ROLE_"+role.toUpperCase());
        List<User> users = userRepository.findUserByRolesName(eRole); // Assume this method exists in your repository
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
//
//    @Override
//    @Transactional
//    public String authenticateUser(String username, String password) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        // Generate and return a token (e.g., JWT token)
//        return "dummy-token"; // Replace with actual token generation logic
//    }
//
//    @Override
//    @Transactional
//    public void changeUserPassword(Long id, String newPassword) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//
//        user.setPassword(passwordEncoder.encode(newPassword)); // Hash new password
//        userRepository.save(user);
//    }
//
    @Override
    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActive(!user.getActive());
        userRepository.save(user);
    }



    private UserDtoResponse convertToDto(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .city(user.getCity())
                .address(user.getAddress())
                .country(user.getCountry())
                .phoneNumber(user.getPhoneNumber())
                .postalCode(user.getPostalCode())
//                .password(user.getPassword())
                .image(user.getImage())
                .active(user.getActive())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public UserDtoResponse uploadUserImage(Long id, MultipartFile image) throws Exception {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));

        String link = imageService.uploadFile(image);

        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        Image image1 = new Image();
        image1.setLink(link);
        imageRepository.save(image1);

        user.setImage(image1);
        return convertToDto(userRepository.save(user));
    }

    @Override
    public UserDtoResponse editUserImage(Long id, MultipartFile image) throws Exception {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        Image oldImage = user.getImage();
        user.setImage(null);
        imageRepository.delete(oldImage);


        String link = imageService.uploadFile(image);

        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        Image image1 = new Image();
        image1.setLink(link);
        imageRepository.save(image1);

        user.setImage(image1);
        return convertToDto(userRepository.save(user));
    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


}