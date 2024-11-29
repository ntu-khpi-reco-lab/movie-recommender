package com.movie.recommender.location.model.dto;

import com.movie.recommender.common.model.location.LocationDTO;
import com.movie.recommender.location.model.entity.City;
import com.movie.recommender.location.model.entity.Location;

public class LocationDTOMapper {

    public static LocationDTO toDTO(Location location) {
        City city = location.getCity();
        String cityName = city.getName();
        String countryName = city.getCountry().getName();
        return new LocationDTO(
                location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getUserId(),
                location.getCreatedAt(),
                cityName,
                countryName
        );
    }
}
