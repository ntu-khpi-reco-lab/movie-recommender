package com.movie.recommender.location.controller;

import com.movie.recommender.location.service.LocationService;
import com.movie.recommender.location.dto.LocationsDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createLocation(@RequestBody @Valid LocationsDTO locationDTO) {
        log.info("Creating location with: latitude: {}, longitude: {}, timestamp: {}, user_id {}",
                locationDTO.getLatitude(),
                locationDTO.getLongitude(),
                LocalDateTime.now(),
                locationDTO.getUser_id());

        LocationsDTO savedDTOLocation = locationService.saveLocation(locationDTO);
        log.info("Location created with ID: {}", savedDTOLocation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTOLocation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id) {
        log.info("Fetching location with ID: {}", id);
        Optional<LocationsDTO> location = locationService.getLocationById(id);

        return location.map(loc -> {
            log.info("Location found: {}", loc);
            return ResponseEntity.ok(loc);
        }).orElseGet(() -> {
            log.warn("Location with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        });
    }
}
