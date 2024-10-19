package com.movie.recommender.location.service;

import com.movie.recommender.location.exception.LocationNotFoundException;
import com.movie.recommender.location.repository.LocationRepository;
import com.movie.recommender.location.model.dto.LocationCreateDTO;
import com.movie.recommender.location.model.dto.LocationUpdateDTO;
import com.movie.recommender.location.model.entity.Location;
import com.movie.recommender.location.model.dto.LocationDTO;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationDTO saveLocation(LocationCreateDTO locationCreateDTO) {
        log.info("Saving new location for user: {}", locationCreateDTO.getUserId());

        Location location = LocationCreateDTO.toEntity(locationCreateDTO);
        Location savedLocation = locationRepository.save(location);

        log.info("New location with ID {} was saved for user {}", savedLocation.getId(), locationCreateDTO.getUserId());
        return LocationDTO.toDTO(savedLocation);
    }

    public LocationDTO getLocationById(Long id) {
        log.info("Fetching location by ID: {}", id);
        return locationRepository.findById(id)
                .map(LocationDTO::toDTO)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with ID: " + id));
    }

    public LocationDTO getLocationByUserId(Long userId) {
        log.info("Fetching location for user ID: {}", userId);
        Location location = findLocationByUserId(userId);
        log.info("Location found for user ID: {}", userId);
        return LocationDTO.toDTO(location);
    }

    @Transactional
    public LocationDTO updateLocationByUserId(Long userId, LocationUpdateDTO locationUpdateDTO) {
        log.info("Updating location for user ID: {}", userId);
        Location location = findLocationByUserId(userId);

        location.setLatitude(locationUpdateDTO.getLatitude());
        location.setLongitude(locationUpdateDTO.getLongitude());

        Location updatedLocation = locationRepository.save(location);
        log.info("Location with ID {} was updated for user ID {}", updatedLocation.getId(), userId);
        return LocationDTO.toDTO(updatedLocation);
    }

    private Location findLocationByUserId(Long userId) {
        return locationRepository.findByUserId(userId)
                .orElseThrow(() -> new LocationNotFoundException("Location not found for user ID: " + userId));
    }
}
