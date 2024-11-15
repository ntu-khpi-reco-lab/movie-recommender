package com.movie.recommender.user.controller;

import com.movie.recommender.user.model.dto.*;
import com.movie.recommender.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
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
}

