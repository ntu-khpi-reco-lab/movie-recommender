package com.movie.recommender.location.repository;

import com.movie.recommender.location.model.dto.CountryCityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.recommender.location.model.entity.Country;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
    @Query("SELECT new com.movie.recommender.location.model.dto.CountryCityDTO(cn.name, ct.name, cn.code) " +
            "FROM City ct JOIN Country cn ON cn.id = ct.country.id")
    List<CountryCityDTO> getAllCountriesAndCities();
}
