package com.movie.recommender.crawler;

import com.movie.recommender.crawler.dataimport.MovieDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        log.info("Crawler started");
        initializeMovieData(context);
    }

    private static void initializeMovieData(ApplicationContext context) {
        String datasetPath = System.getenv("DATASETPATH");
        if (datasetPath == null) {
            log.error("DATASETPATH environment variable is not set.");
            return;
        }

        // Initialize movie data
        MovieDataInitializer dataInitializer = context.getBean(MovieDataInitializer.class);
        dataInitializer.initializeData(datasetPath);
    }
}
