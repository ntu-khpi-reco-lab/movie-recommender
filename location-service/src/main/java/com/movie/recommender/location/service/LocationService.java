package com.movie.recommender.location.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.recommender.location.exception.LocationNotFoundException;
import com.movie.recommender.location.model.entity.City;
import com.movie.recommender.location.model.entity.Country;
import com.movie.recommender.location.repository.CityRepository;
import com.movie.recommender.location.repository.CountryRepository;
import com.movie.recommender.location.repository.LocationRepository;
import com.movie.recommender.location.model.dto.LocationCreateDTO;
import com.movie.recommender.location.model.dto.LocationUpdateDTO;
import com.movie.recommender.location.model.entity.Location;
import com.movie.recommender.location.model.dto.LocationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;

    public LocationService(LocationRepository locationRepository, CityRepository cityRepository, CountryRepository countryRepository, RestTemplate restTemplate) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public LocationDTO saveLocation(LocationCreateDTO locationCreateDTO) {
        log.info("Saving new location for user: {}", locationCreateDTO.getUserId());
        log.info("Fetching or creating city for user: {}", locationCreateDTO.getUserId());

        String cityName;
        String countryName;

        try {
            String GEOLOCATION_URL = "https://geocode.xyz/%s,%s?geoit=json";
            ResponseEntity<String> response = restTemplate.getForEntity(
                    String.format(GEOLOCATION_URL, locationCreateDTO.getLatitude(), locationCreateDTO.getLongitude()),
                    String.class
            );
            JsonNode jsonNode = (new ObjectMapper()).readTree(response.getBody());
            cityName = jsonNode.get("city").asText();
            countryName = jsonNode.get("country").asText();
            log.info("Saving new location for city: {} and country: {}", cityName, countryName);

        } catch (Exception e){
            throw new LocationNotFoundException(e.getMessage());
        }

        log.info("Fetching city: {} and country: {}", cityName, countryName);
        Country country = countryRepository.findByName(countryName)
                .orElseGet(() -> {
                    log.info("Creating new country: {}", countryName);
                    Country newCountry = new Country();
                    newCountry.setName(countryName);
                    log.info("New country {} was successfully created", newCountry);
                    return countryRepository.save(newCountry);
                });

        City city = cityRepository.findByNameAndCountry(cityName, country)
                .orElseGet(() -> {
                    log.info("Creating new city: {}", cityName);
                    City newCity = new City();
                    newCity.setName(cityName);
                    newCity.setCountry(country);
                    log.info("New city {} was successfully created", newCity);
                    return cityRepository.save(newCity);
                });

        locationCreateDTO.setCity(city);

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
