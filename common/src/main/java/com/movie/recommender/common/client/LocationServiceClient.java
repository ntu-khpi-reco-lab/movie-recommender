package com.movie.recommender.common.client;

import com.movie.recommender.common.model.location.CountryWithCitiesDTO;
import com.movie.recommender.common.model.location.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "location-service", url = "${service.location.service.url}")
public interface LocationServiceClient {
    @GetMapping("/api/v1/locations/countries/cities")
    List<CountryWithCitiesDTO> getAllCountriesAndCities();
    @GetMapping("api/v1/locations/users/")
    LocationDTO getLocationByUserId(@RequestHeader("Authorization") String token);
}
