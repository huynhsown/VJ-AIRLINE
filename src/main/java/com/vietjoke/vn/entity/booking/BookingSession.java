package com.vietjoke.vn.entity.booking;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SelectFlightParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.*;

@RedisHash("booking_session")
@Getter
@Setter
public class BookingSession {
    @Id
    private String sessionId;

    private SearchParamDTO searchCriteria;
    private SelectFlightRequestDTO selectedFlight;
    private PassengersInfoParamDTO passengersInfoParamDTO;
    private Set<String> lockedSeats = new HashSet<>();
    private LocalDateTime createdAt;

    @TimeToLive
    private Long timeToLive;

    public BookingSession() {
        sessionId = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
    }

    public LocalDateTime getExpireAt(){
        return this.createdAt.plusSeconds(this.timeToLive);
    }
}
