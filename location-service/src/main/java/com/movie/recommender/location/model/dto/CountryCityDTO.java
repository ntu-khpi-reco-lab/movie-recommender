package com.movie.recommender.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCityDTO {
    private String countryName;
    private String cityName;
}
