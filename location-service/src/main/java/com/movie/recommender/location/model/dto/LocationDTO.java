package com.movie.recommender.location.model.dto;

import com.movie.recommender.location.model.entity.Location;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long id;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    private double longitude;

    private Long userId;
    private LocalDateTime createdAt;

    public static LocationDTO toDTO(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getUserId(),
                location.getCreatedAt()
        );
    }
}
