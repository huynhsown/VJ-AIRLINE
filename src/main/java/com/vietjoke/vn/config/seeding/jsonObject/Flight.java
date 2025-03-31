package com.vietjoke.vn.config.seeding.jsonObject;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Flight {
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private String gate;
    private String terminal;
    private String airlineCode;
    private String routeCode;
    private String registerNumber;
    private String statusCode;

    private List<FareAvailbility> fareAvailability;
}