package com.movie.recommender.location.controller;

import com.movie.recommender.location.model.dto.LocationCreateDTO;
import com.movie.recommender.location.model.dto.LocationUpdateDTO;
import com.movie.recommender.location.service.LocationService;
import org.springframework.web.server.ResponseStatusException;
import com.movie.recommender.location.model.dto.LocationDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationCreateDTO locationCreateDTO) {
        log.info("Creating location: {}", locationCreateDTO);
        LocationDTO savedDTOLocation = locationService.saveLocation(locationCreateDTO);
        log.info("Location created with ID: {}", savedDTOLocation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTOLocation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id) {
        log.info("Fetching location with ID: {}", id);
        LocationDTO location = locationService.getLocationById(id)
                .orElseThrow(() -> {
                    log.warn("Location with ID: {} not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
                });

        log.info("Location found: {}", location);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getLocationByUserId(@PathVariable("userId") Long userId) {
        log.info("Fetching location with user id: {}", userId);
        LocationDTO location = locationService.getLocationByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Location with user id: {} not found", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
                });

        log.info("Location found: {}", location);
        return ResponseEntity.ok(location);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<LocationDTO> updateLocationByUserId(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid LocationUpdateDTO locationUpdateDTO) {
        log.info("Updating location for user with ID: {}", userId);
        LocationDTO updatedLocation = locationService.updateLocationByUserId(userId, locationUpdateDTO);
        log.info("Location updated for user with ID: {}", userId);
        return ResponseEntity.ok(updatedLocation);
    }
}
