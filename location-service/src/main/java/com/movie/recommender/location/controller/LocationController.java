package com.movie.recommender.location.controller;

import com.movie.recommender.location.model.dto.*;
import com.movie.recommender.location.service.LocationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import java.util.List;


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
        LocationDTO savedLocation = locationService.saveLocation(locationCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("id") Long id) {
        LocationDTO location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<LocationDTO> getLocationByUserId(@PathVariable("userId") Long userId) {
        LocationDTO location = locationService.getLocationByUserId(userId);
        return ResponseEntity.ok(location);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<LocationDTO> updateLocationForUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid LocationUpdateDTO locationUpdateDTO) {
        LocationDTO updatedLocation = locationService.updateLocationByUserId(userId, locationUpdateDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/countries/cities")
    public ResponseEntity<List<CountryWithCitiesDTO>> getAllCountriesAndCities() {
        List<CountryWithCitiesDTO> result = locationService.getAllCountriesAndCities();
        return ResponseEntity.ok(result);
    }
}
