package com.movie.recommender.user.controller;

import com.movie.recommender.user.config.JwtUtil;
import com.movie.recommender.user.model.dto.UserCreateDTO;
import com.movie.recommender.user.model.dto.UserDTO;
import com.movie.recommender.user.model.dto.UserLoginDTO;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = userService.registerUser(userCreateDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return ResponseEntity.ok(token);
    }
}

