package com.movie.recommender.location.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeolocationResult{
    private String city;
    private String country;
    @JsonProperty("prov")
    private String countryCode;
}
