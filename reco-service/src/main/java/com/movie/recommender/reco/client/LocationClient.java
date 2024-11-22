package com.movie.recommender.reco.client;

import com.movie.recommender.location.model.dto.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "location-service", url = "http://localhost:9001/api/v1/locations")
public interface LocationClient {

    @GetMapping("/users/{userId}")
    LocationDTO getLocationByUserId(@PathVariable("userId") Long userId);
}