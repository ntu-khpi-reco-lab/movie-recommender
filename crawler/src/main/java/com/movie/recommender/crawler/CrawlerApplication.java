package com.movie.recommender.crawler;

import com.movie.recommender.crawler.dataimport.MovieDataInitializer;
import com.movie.recommender.crawler.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Slf4j
@EnableAsync
@EnableFeignClients(basePackages = "com.movie.recommender.common.client")
public class CrawlerApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);
        log.info("Crawler started");
        initializeMovieData(context);
        loadNowPlayingMovies(context);

    }

    private static void initializeMovieData(ApplicationContext context) {
        MovieDataInitializer dataInitializer = context.getBean(MovieDataInitializer.class);
        dataInitializer.initializeData();
    }

    private static void loadNowPlayingMovies(ApplicationContext context) {
            MovieService movieService = context.getBean(MovieService.class);
            movieService.processCountryMovies();
    }

}
