package com.movie.recommender.reco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.movie.recommender.reco.client")
public class RecoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecoServiceApplication.class, args);
    }
}