package com.movie.recommender.crawler;

import com.movie.recommender.crawler.dataimport.MovieDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
        "com.movie.recommender.common.security",
        "com.movie.recommender.common.config",
        "com.movie.recommender.common.client",
        "com.movie.recommender.crawler"
})
@Slf4j
@EnableAsync
@EnableFeignClients(basePackages = "com.movie.recommender.common.client")
public class CrawlerApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);
        log.info("Crawler application started successfully");
        initializeMovieData(context);
    }

    private static void initializeMovieData(ApplicationContext context) {
        log.info("Initializing movie data");
        MovieDataInitializer dataInitializer = context.getBean(MovieDataInitializer.class);
        dataInitializer.initializeData();
    }
}