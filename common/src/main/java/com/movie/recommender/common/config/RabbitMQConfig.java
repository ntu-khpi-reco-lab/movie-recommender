package com.movie.recommender.common.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.movie.recommender.common.model.location.LocationExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import com.movie.recommender.common.model.location.LocationQueue;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQConfig {
    public static final String NEW_LOCATION_QUEUE = "location.update.queue";
    public static final String NEW_LOCATION_EXCHANGE = "location.exchange";
    public static final String NEW_LOCATION_ROUTINGKEY = "location.update";

    @Bean
    public LocationQueue queue() {
        return new LocationQueue(NEW_LOCATION_QUEUE, true);
    }

    @Bean
    public LocationExchange exchange() {
        return new LocationExchange(NEW_LOCATION_EXCHANGE);
    }

    @Bean
    public Binding binding(LocationQueue queue, LocationExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(NEW_LOCATION_ROUTINGKEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}