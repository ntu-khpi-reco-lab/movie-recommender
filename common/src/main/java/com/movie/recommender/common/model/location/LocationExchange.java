package com.movie.recommender.common.model.location;

import org.springframework.amqp.core.TopicExchange;

public class LocationExchange extends TopicExchange {
    public LocationExchange(String name) {
        super(name);
    }
}
