package com.movie.recommender.location.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LocationServiceConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
