package com.movie.recommender.location.controller;


import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.location.LocationDTO;
import com.movie.recommender.location.security.CustomPrincipal;
import com.movie.recommender.location.service.LocationService;
import com.movie.recommender.location.model.dto.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<LocationDTO> createLocation(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody @Valid LocationCreateDTO locationCreateDTO) {
        locationCreateDTO.setUserId(principal.getUserId());
        LocationDTO savedLocation = locationService.saveLocation(locationCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("id") Long id) {
        LocationDTO location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/users/")
    public ResponseEntity<LocationDTO> getLocationByUserId(@AuthenticationPrincipal CustomPrincipal principal) {
        LocationDTO location = locationService.getLocationByUserId(principal.getUserId());
        return ResponseEntity.ok(location);
    }

    @PutMapping("/users/")
    public ResponseEntity<LocationDTO> updateLocationForUser(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody @Valid LocationUpdateDTO locationUpdateDTO) {
        LocationDTO updatedLocation = locationService.updateLocationByUserId(principal.getUserId(), locationUpdateDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/countries/cities")
    public ResponseEntity<List<CountryWithCitiesDTO>> getAllCountriesAndCities() {
        List<CountryWithCitiesDTO> result = locationService.getAllCountriesAndCities();
        return ResponseEntity.ok(result);
    }
}
