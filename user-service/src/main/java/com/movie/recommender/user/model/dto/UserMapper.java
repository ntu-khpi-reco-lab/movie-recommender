package com.movie.recommender.user.model.dto;

import com.movie.recommender.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }
}

