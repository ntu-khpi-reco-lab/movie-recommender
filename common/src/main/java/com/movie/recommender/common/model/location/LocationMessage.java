package com.movie.recommender.common.model.location;

import lombok.Data;
import java.io.Serializable;

@Data
public class LocationMessage implements Serializable {
    private String countryCode;
    private String cityName;

    public LocationMessage(String countryCode, String cityName) {
        this.countryCode = countryCode;
        this.cityName = cityName;
    }
}
