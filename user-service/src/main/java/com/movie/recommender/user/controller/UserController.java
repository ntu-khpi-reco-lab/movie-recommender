package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.*;
import com.movie.recommender.common.security.CustomPrincipal;
import com.movie.recommender.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal CustomPrincipal principal) {
        UserDTO userDTO = userService.getUserProfile(principal.getUserId());
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserDTO userDTO = userService.updateUserProfile(principal.getUserId(), userUpdateDTO);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUserProfile(@AuthenticationPrincipal CustomPrincipal principal) {
        userService.deleteUserProfile(principal.getUserId());
        return ResponseEntity.noContent().build();
    }
}

