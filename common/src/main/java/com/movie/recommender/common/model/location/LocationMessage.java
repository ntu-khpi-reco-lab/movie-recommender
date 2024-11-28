package com.movie.recommender.common.model.location;

import lombok.Data;
import java.io.Serializable;

@Data
public class LocationMessage implements Serializable {
    private String message;
    private String countryCode;
    private String cityName;

    public LocationMessage(String message) {
        this.message = message;
        this.countryCode = "";
        this.cityName = "";
    }

    public LocationMessage(String message, String countryCode, String cityName) {
        this.message = message;
        this.countryCode = countryCode;
        this.cityName = cityName;
    }
}
