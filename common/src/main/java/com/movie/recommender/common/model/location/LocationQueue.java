package com.movie.recommender.common.model.location;

import org.springframework.amqp.core.Queue;

public class LocationQueue extends Queue {
    public LocationQueue(String name, boolean durable) {
        super(name, durable);
    }
}
