package com.movie.recommender.location.service;

import com.movie.recommender.location.exception.LocationNotFoundException;
import com.movie.recommender.location.model.entity.GeolocationResult;
import com.movie.recommender.location.repository.LocationRepository;
import com.movie.recommender.location.repository.CountryRepository;
import com.movie.recommender.location.model.dto.LocationCreateDTO;
import com.movie.recommender.location.model.dto.LocationUpdateDTO;
import com.movie.recommender.location.repository.CityRepository;
import com.movie.recommender.location.model.entity.Location;
import com.movie.recommender.location.model.dto.LocationDTO;
import com.movie.recommender.location.model.entity.Country;
import com.movie.recommender.location.model.entity.City;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GeolocationService geolocationService;

    public LocationService(LocationRepository locationRepository, CityRepository cityRepository, CountryRepository countryRepository, GeolocationService geolocationService) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.geolocationService = geolocationService;
    }

    @Transactional
    public City getCityByCoordinates(Double latitude, Double longitude) {
        log.info("Fetching or creating geolocation");

        GeolocationResult result;
        result = geolocationService.fetchGeolocation(latitude, longitude);

        String cityName = result.getCity();
        String countryName = result.getCountry();

        log.info("Fetching city: {} and country {} from db", cityName, countryName);
        Country country = findOrCreateCountryByName(countryName);
        return findOrCreateCityByName(cityName, country);
    }

    @Transactional
    public Country findOrCreateCountryByName(String name) {
        log.info("Finding country by name: {}", name);
        return countryRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Creating new country: {}", name);
                    Country newCountry = new Country();
                    newCountry.setName(name);
                    log.info("New country {} was successfully created", newCountry);
                    return countryRepository.save(newCountry);
                });
    }

    @Transactional
    public City findOrCreateCityByName(String name, Country country) {
        log.info("Finding city by name: {}", name);
        return cityRepository.findByNameAndCountry(name, country)
                .orElseGet(() -> {
                    log.info("Creating new city: {}", name);
                    City newCity = new City();
                    newCity.setName(name);
                    newCity.setCountry(country);
                    log.info("New city {} was successfully created", newCity);
                    return cityRepository.save(newCity);
                });
    }

    @Transactional
    public LocationDTO saveLocation(LocationCreateDTO locationCreateDTO) {
        log.info("Saving new location for user: {}", locationCreateDTO.getUserId());
        City city = getCityByCoordinates(locationCreateDTO.getLatitude(), locationCreateDTO.getLongitude());

        Location location = LocationCreateDTO.toEntity(locationCreateDTO);
        location.setCity(city);
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
        City city = getCityByCoordinates(locationUpdateDTO.getLatitude(), locationUpdateDTO.getLongitude());

        Location location = findLocationByUserId(userId);

        location.setCity(city);
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
