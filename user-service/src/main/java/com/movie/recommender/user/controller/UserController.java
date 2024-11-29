package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.*;
import com.movie.recommender.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = userService.registerUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") Long userId) {
        UserDTO userDTO = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserDTO> updateUserProfile(@PathVariable("userId") Long userId, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserDTO userDTO = userService.updateUserProfile(userId, userUpdateDTO);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{userId}/profile")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable("userId") Long userId) {
        userService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
}

