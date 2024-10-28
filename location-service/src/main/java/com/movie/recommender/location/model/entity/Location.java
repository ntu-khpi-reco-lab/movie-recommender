package com.movie.recommender.location.model.entity;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    @Column(nullable = false)
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    @Column(nullable = false)
    private double longitude;

    @Column(unique = true, nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    public Location(double latitude, double longitude, Long userId, City city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.city = city;
    }
}
