package com.example.posapplicationapis.restcontroller.User;

import com.example.posapplicationapis.dto.user.UserAuthDto;
import com.example.posapplicationapis.dto.user.UserAuthDtoResponse;
import com.example.posapplicationapis.dto.user.UserDtoRequest;
import com.example.posapplicationapis.dto.user.UserDtoResponse;
import com.example.posapplicationapis.services.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserServiceImpl userService;

    @PostMapping("/add-user")
    public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse createdUser = userService.createUser(userDtoRequest);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}/get-user")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        UserDtoResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        List<UserDtoResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = "/{id}/update-user" )
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse updatedUser = userService.updateUser(id, userDtoRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}/remove-user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{role}/get-users-by-role")
    public ResponseEntity<List<UserDtoResponse>> getUsersByRole(@PathVariable String role) {
        List<UserDtoResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}/activate-deactivate")
    public ResponseEntity<Boolean> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(userService.getUserById(id).getActive());
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<UserDtoResponse> uploadUserImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws Exception {

        return ResponseEntity.ok(userService.uploadUserImage(id, image));
    }
    @PutMapping("/{id}/edit-image")
    public ResponseEntity<UserDtoResponse> editUserImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws Exception {

        return ResponseEntity.ok(userService.editUserImage(id, image));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthDtoResponse> authenticateUser(@RequestBody UserAuthDto userDtoRequest) {
        try {
            UserAuthDtoResponse userAuthDtoResponse = userService.authenticateUser(userDtoRequest.getName(), userDtoRequest.getPassword());
            return ResponseEntity.ok(userAuthDtoResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changeUserPassword(@PathVariable Long id, @RequestBody String newPassword) {
        try {
            userService.changeUserPassword(id, newPassword);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // 404 Not Found if user doesn't exist
        }
    }


}
