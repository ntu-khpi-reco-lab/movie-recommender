package com.movie.recommender.common.model.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LocationMessage {
    private String countryCode;
    private String cityName;

    @JsonCreator
    public LocationMessage(
            @JsonProperty("countryCode") String countryCode,
            @JsonProperty("cityName") String cityName) {
        this.countryCode = countryCode;
        this.cityName = cityName;
    }
}
