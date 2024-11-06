package com.movie.recommender.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCityDTO {
    private String countryName;
    private String cityName;
}
