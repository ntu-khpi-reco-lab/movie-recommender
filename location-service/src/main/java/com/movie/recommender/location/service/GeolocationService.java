package com.movie.recommender.location.service;

import com.movie.recommender.location.exception.GeolocationFetchException;
import com.movie.recommender.location.model.dto.GeolocationResult;
import com.movie.recommender.lib.http.HttpClient;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeolocationService {
    private static final String GEOLOCATION_URL = "https://geocode.xyz/%s,%s?geoit=json";
    private final HttpClient httpClient = new HttpClient();

    public GeolocationResult fetchGeolocation(double latitude, double longitude) {
        log.info("Fetching Geolocation for latitude {} and longitude {}", latitude, longitude);
        try {
            String url = String.format(GEOLOCATION_URL, latitude, longitude);
            return httpClient.get(url, GeolocationResult.class);
        } catch (Exception e) {
            log.error("Error fetching geolocation: {}", e.getMessage(), e);
            throw new GeolocationFetchException("Failed to fetch geolocation data from external API", e);
        }
    }
}
