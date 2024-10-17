package com.movie.recommender.location.dto;

import com.movie.recommender.location.model.entity.Locations;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationsDTO {
    private Long id;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    private double longitude;

    private Long user_id;
    private LocalDateTime createdAt;

    public String toString(){
        return "id: " + id + ", latitude: " + latitude + ", longitude: " + longitude + ", user_id: " + user_id + ", created_at: " + createdAt;
    }

    public static LocationsDTO toDTO(Locations locations) {
        LocationsDTO dto = new LocationsDTO();
        dto.setId(locations.getId());
        dto.setLatitude(locations.getLatitude());
        dto.setLongitude(locations.getLongitude());
        dto.setUser_id(locations.getUser_id());
        dto.setCreatedAt(locations.getCreatedAt());
        return dto;
    }

    public static Locations toEntity(LocationsDTO dto) {
        Locations locations = new Locations();
        locations.setId(dto.getId());
        locations.setLatitude(dto.getLatitude());
        locations.setLongitude(dto.getLongitude());
        locations.setUser_id(dto.getUser_id());
        locations.setCreatedAt(dto.getCreatedAt());
        return locations;
    }
}
