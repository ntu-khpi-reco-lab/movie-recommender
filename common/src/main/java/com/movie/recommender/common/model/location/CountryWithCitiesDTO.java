package com.movie.recommender.common.model.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryWithCitiesDTO {
    @JsonProperty("countryName")
    private String countryName;
    @JsonProperty("cities")
    private List<String> cities;
    @JsonProperty("countryCode")
    private String countryCode;
}
