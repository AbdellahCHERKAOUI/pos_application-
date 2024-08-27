package com.example.posapplicationapis.services.user;

import com.example.posapplicationapis.dto.user.UserDtoRequest;
import com.example.posapplicationapis.dto.user.UserDtoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);
    UserDtoResponse getUserById(Long id);
    List<UserDtoResponse> getAllUsers();
    UserDtoResponse updateUser(Long id, UserDtoRequest userDtoRequest);
    void deleteUser(Long id);
    List<UserDtoResponse> getUsersByRole(String role);
    Long authenticateUser(String username, String password);
    void changeUserPassword(Long id, String newPassword);
    void activateUser(Long id);
    UserDtoResponse uploadUserImage(Long id, MultipartFile image) throws Exception;
    UserDtoResponse editUserImage(Long id, MultipartFile image) throws Exception;
}
