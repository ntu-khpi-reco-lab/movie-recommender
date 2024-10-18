package com.movie.recommender.location.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String message;
}
