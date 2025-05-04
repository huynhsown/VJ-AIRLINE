package com.vietjoke.vn.dto.response.flight;

import com.vietjoke.vn.util.enums.user.PassengerType;
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
@AllArgsConstructor
@NoArgsConstructor
public class BookingPreviewDTO {
    private String sessionToken;
    private List<FlightSummaryDTO> flights = new ArrayList<>();
    private List<PassengerBookingDetailDTO> passengerDetails = new ArrayList<>();
    private String coupon;
    private BigDecimal totalBookingPrice;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlightSummaryDTO {
        private String flightNumber;
        private BigDecimal totalTicketPrice;
        private BigDecimal totalAddonPrice;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassengerBookingDetailDTO {
        private String passengerUuid;
        private String firstName;
        private String lastName;
        private PassengerType passengerType;
        private List<PassengerFlightDetailDTO> passengerFlightDetailDTOS = new ArrayList<>();
        private String accompanyingAdultFirstName;
        private String accompanyingAdultLastName;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassengerFlightDetailDTO {
        private String flightNumber;
        private String fareClass;
        private String routeCode;
        private BigDecimal ticketPrice;
        private BigDecimal addonPrice;
        private List<AddonDetailDTO> addons = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddonDetailDTO {
        private Long addonId;
        private String addonName;
        private Integer quantity;
        private BigDecimal price;
    }
}
