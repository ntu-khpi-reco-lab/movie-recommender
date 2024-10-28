package com.movie.recommender.location.repository;

import com.movie.recommender.location.model.entity.City;
import com.movie.recommender.location.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndCountry(String name, Country country);
}
