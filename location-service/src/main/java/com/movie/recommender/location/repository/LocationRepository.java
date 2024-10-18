package com.movie.recommender.location.repository;

import com.movie.recommender.location.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByUserId(Long userId);
}
