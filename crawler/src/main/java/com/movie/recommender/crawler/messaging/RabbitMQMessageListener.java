package com.movie.recommender.crawler.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.movie.recommender.common.model.location.LocationMessage;
import com.movie.recommender.common.config.RabbitMQConfig;
import com.movie.recommender.crawler.service.MovieService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RabbitMQMessageListener {
    final private MovieService movieService;

    public RabbitMQMessageListener(MovieService movieService) {
        this.movieService = movieService;
    }

    @RabbitListener(queues = RabbitMQConfig.NEW_LOCATION_QUEUE)
    public void handleNewLocationMessage(LocationMessage message) {
        try {
            if (message == null || message.getCountryCode().isBlank() || message.getCityName().isBlank()) {
                log.warn("Received an empty or null message. Skipping processing.");
                return;
            }
            log.info("Received crawl message with city {} and country code {}", message.getCityName(), message.getCountryCode());
            log.info("Processing crawl message...");
            movieService.processCountryMovies();
            log.info("Crawl process completed.");
        } catch (Exception e) {
            log.error("Error during processing crawl message: {}", e.getMessage(), e);
        }
    }
}
