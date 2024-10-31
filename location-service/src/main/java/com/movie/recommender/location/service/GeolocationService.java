package com.movie.recommender.location.service;

import com.movie.recommender.location.exception.LocationNotFoundException;
import com.movie.recommender.location.model.entity.GeolocationResult;
import com.movie.recommender.lib.http.HttpClient;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;


@Slf4j
@Service
public class GeolocationService {
    private final String GEOLOCATION_URL = "https://geocode.xyz/%s,%s?geoit=json";
    private final HttpClient httpClient = new HttpClient();

    public GeolocationResult fetchGeolocation(double latitude, double longitude) {
        log.info("Fetching Geolocation for latitude {} and longitude {}", latitude, longitude);
        GeolocationResult result;
        try {
            String url = String.format(GEOLOCATION_URL, latitude, longitude);
            result = httpClient.get(url, GeolocationResult.class);
        } catch (IOException e) {
            log.error("Error fetching geolocation: {}", e.getMessage(), e);
            throw new LocationNotFoundException(e.getMessage());
        }

        return result;
    }
}
