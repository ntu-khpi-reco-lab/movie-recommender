package com.movie.recommender.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    // Test endpoint
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
