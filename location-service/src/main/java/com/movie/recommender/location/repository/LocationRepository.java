package com.movie.recommender.location.repository;

import com.movie.recommender.location.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Long> {
}
