package com.movie.recommender.location.service;

import com.movie.recommender.location.dto.LocationsDTO;
import com.movie.recommender.location.repository.LocationRepository;
import com.movie.recommender.location.model.entity.Locations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationsDTO saveLocation(LocationsDTO locationDTO) {
        log.info("Converting DTO to entity and saving location: {}", locationDTO);
        Locations location = LocationsDTO.toEntity(locationDTO);
        Locations savedLocation = locationRepository.save(location);
        return LocationsDTO.toDTO(savedLocation);
    }

    public Optional<LocationsDTO> getLocationById(Long id) {
        log.info("Getting location by id: {}", id);
        return locationRepository.findById(id).map(LocationsDTO::toDTO);
    }
}
