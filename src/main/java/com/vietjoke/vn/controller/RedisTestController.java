package com.vietjoke.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test() {
        try {
            String pong = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().ping();
            return "Redis ping response: " + pong;
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/test-redis")
    public String testRedis() {
        return "HELLO";
    }
}