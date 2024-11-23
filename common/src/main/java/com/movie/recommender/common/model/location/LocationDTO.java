package com.movie.recommender.common.model.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long id;
    private double latitude;  // Уберите аннотации
    private double longitude; // Уберите аннотации
    private Long userId;
    private LocalDateTime createdAt;
    private String cityName;
    private String countryName;
}