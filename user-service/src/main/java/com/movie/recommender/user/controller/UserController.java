package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.*;
import com.movie.recommender.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/profile")
        public ResponseEntity<UserDTO> getUserProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO userDTO = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(HttpServletRequest request, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO userDTO = userService.updateUserProfile(userId, userUpdateDTO);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUserProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
}

