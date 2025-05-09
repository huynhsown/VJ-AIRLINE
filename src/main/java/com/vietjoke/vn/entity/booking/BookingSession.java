package com.vietjoke.vn.entity.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@RedisHash("booking_session")
@Getter
@Setter
@Slf4j
public class BookingSession implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Id
    private String sessionId;

    private SearchParamDTO searchCriteria;
    private SelectFlightRequestDTO selectedFlight;
    private PassengersInfoParamDTO passengersInfoParamDTO;
    private Set<String> lockedSeats = new HashSet<>();

    private String passengerAddonsJson;
    
    @Transient
    private Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons = new HashMap<>();
    
    private BookingSessionStep currentStep;
    private BookingSessionStep nextStep;
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
    
    public Map<String, Map<String, List<AddonSelectionDTO>>> getPassengerAddons() {
        if (passengerAddons == null || passengerAddons.isEmpty()) {
            if (passengerAddonsJson != null && !passengerAddonsJson.isEmpty()) {
                try {
                    passengerAddons = objectMapper.readValue(passengerAddonsJson, 
                            new TypeReference<Map<String, Map<String, List<AddonSelectionDTO>>>>() {});
                } catch (JsonProcessingException e) {
                    log.error("Error deserializing passengerAddons: {}", e.getMessage());
                    passengerAddons = new HashMap<>();
                }
            } else {
                passengerAddons = new HashMap<>();
            }
        }
        return passengerAddons;
    }
    
    public void setPassengerAddons(Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons) {
        this.passengerAddons = passengerAddons;
        try {
            this.passengerAddonsJson = objectMapper.writeValueAsString(passengerAddons);
        } catch (JsonProcessingException e) {
            log.error("Error serializing passengerAddons: {}", e.getMessage());
            this.passengerAddonsJson = "{}";
        }
    }
}
