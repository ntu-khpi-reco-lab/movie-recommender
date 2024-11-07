package com.movie.recommender.common.model.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryWithCitiesDTO {
    private String countryName;
    private List<String> cities;
}
