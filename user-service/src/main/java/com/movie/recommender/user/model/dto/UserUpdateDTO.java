package com.movie.recommender.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
}