package com.movie.recommender.location.model.dto;

import com.movie.recommender.location.model.entity.Location;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateDTO {
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    private double longitude;

    private Long userId;

    public static Location toEntity(LocationCreateDTO locationCreateDTO) {
        return new Location(
                locationCreateDTO.getLatitude(),
                locationCreateDTO.getLongitude(),
                locationCreateDTO.getUserId()
        );
    }
}
