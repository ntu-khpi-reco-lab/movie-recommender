package com.movie.recommender.location.controller;

import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.location.config.RabbitMQConfig;
import com.movie.recommender.location.service.LocationService;
import com.movie.recommender.location.model.dto.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;

    public LocationController(LocationService locationService, RabbitTemplate rabbitTemplate) {
        this.locationService = locationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationCreateDTO locationCreateDTO) {
        LocationDTO savedLocation = locationService.saveLocation(locationCreateDTO);
        triggerCrawlProcess("create");
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
        triggerCrawlProcess("update");
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/countries/cities")
    public ResponseEntity<List<CountryWithCitiesDTO>> getAllCountriesAndCities() {
        List<CountryWithCitiesDTO> result = locationService.getAllCountriesAndCities();
        return ResponseEntity.ok(result);
    }

    private void triggerCrawlProcess(String action) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    "location.update",
                    action
            );
            log.info("Message sent to RabbitMQ: {}", action);
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", e.getMessage());
        }
    }
}
