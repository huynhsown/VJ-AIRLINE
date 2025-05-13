package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.util.enums.booking.BookingStatus;
import com.vietjoke.vn.util.enums.flight.TripType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingHistoryDTO {
    private String bookingReference;
    private BookingStatus statusCode;
    private String statusName;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String currency;
    private LocalDateTime bookingDate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private TripType tripType;

    private int adultCount;
    private int childCount;
    private int infantCount;

    List<FlightSimplifyDTO> flights = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightSimplifyDTO{
        private String flightNumber;
        private String routeCode;
        private LocalDateTime departureTime;
    }
}
