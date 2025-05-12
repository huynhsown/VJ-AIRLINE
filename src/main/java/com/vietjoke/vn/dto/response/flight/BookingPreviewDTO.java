package com.vietjoke.vn.dto.response.flight;

import com.vietjoke.vn.dto.pricing.PromoDTO;
import com.vietjoke.vn.util.enums.user.Gender;
import com.vietjoke.vn.util.enums.user.IdType;
import com.vietjoke.vn.util.enums.user.PassengerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private PromoDTO coupon;
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
        private String accompanyingAdultFirstName;
        private String accompanyingAdultLastName;
        private LocalDate dateOfBirth;
        private Gender gender;
        private String countryCode;
        private IdType idType;
        private String idNumber;
        private String phone;
        private String email;
        private List<PassengerFlightDetailDTO> passengerFlightDetailDTOS = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassengerFlightDetailDTO {
        private String flightNumber;
        private String fareClass;
        private String fareCode;
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
