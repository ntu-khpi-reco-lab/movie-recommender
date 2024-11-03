package com.movie.recommender.user.service;

import com.movie.recommender.user.config.JwtUtil;
import com.movie.recommender.user.exception.UserNotFoundException;
import com.movie.recommender.user.model.dto.UserCreateDTO;
import com.movie.recommender.user.model.dto.UserDTO;
import com.movie.recommender.user.model.entity.User;
import com.movie.recommender.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserDTO registerUser(UserCreateDTO userCreateDTO) {
        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = userCreateDTO.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return UserDTO.toDTO(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
}
