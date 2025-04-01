package com.vietjoke.vn.entity.booking;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RedisHash("booking_session")
@Getter
@Setter
public class BookingSession {
    @Id
    private String sessionId;

    private Map<String, Object> searchCriteria;
    private Map<String, Object> searchFlight;
    private LocalDateTime createdAt;

    @TimeToLive
    @Value("${session.timeToLive}")
    private Long timeToLive;

    public BookingSession() {
        sessionId = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
    }
}
