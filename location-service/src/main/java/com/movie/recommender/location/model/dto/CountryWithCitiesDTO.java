package com.movie.recommender.location.model.dto;

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
