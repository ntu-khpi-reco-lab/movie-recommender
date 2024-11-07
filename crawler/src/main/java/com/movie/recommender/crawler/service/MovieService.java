package com.movie.recommender.crawler.service;

import com.movie.recommender.common.client.LocationServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovieService {
    private final LocationServiceClient locationServiceClient;

    public MovieService(LocationServiceClient locationServiceClient) {
        this.locationServiceClient = locationServiceClient;
    }

    // For testing purposes. Verify that the country with cities is retrieved.
    // Should be removed later.
    public void getCountryWithCities() {
        log.info("Getting country with cities...");
        locationServiceClient.getAllCountriesAndCities().forEach(countryWithCitiesDTO -> {
            log.info("Country: {}", countryWithCitiesDTO.getCountryName());
            countryWithCitiesDTO.getCities().forEach(city -> log.info("City: {}", city));
        });
    }
}
