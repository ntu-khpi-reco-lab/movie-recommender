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

import java.util.Optional;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationDTO saveLocation(LocationCreateDTO locationCreateDTO) {
        log.info("Converting DTO to entity and saving location: {}", locationCreateDTO);
        Location location = LocationCreateDTO.toEntity(locationCreateDTO);
        Location savedLocation = locationRepository.save(location);
        log.info("New location {} was saved", savedLocation);
        return LocationDTO.toDTO(savedLocation);
    }

    public Optional<LocationDTO> getLocationById(Long id) {
        log.info("Getting location by id: {}", id);
        return locationRepository.findById(id).map(LocationDTO::toDTO);
    }

    public Optional<LocationDTO> getLocationByUserId(Long userId) {
        log.info("Getting location by user id: {}", userId);
        return locationRepository.findByUserId(userId).map(LocationDTO::toDTO);
    }

    @Transactional
    public LocationDTO updateLocationByUserId(Long userId, LocationUpdateDTO locationUpdateDTO) {
        log.info("Updating location by id: {}", userId);
        Location location = locationRepository.findByUserId(userId)
                .orElseThrow(LocationNotFoundException::new);
        log.info("Location with user id {} was found", userId);
        location.setLatitude(locationUpdateDTO.getLatitude());
        location.setLongitude(locationUpdateDTO.getLongitude());

        Location updatedLocation = locationRepository.save(location);
        log.info("Location with id {} was updated", updatedLocation.getId());
        return LocationDTO.toDTO(updatedLocation);
    }
}
