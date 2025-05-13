package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import com.vietjoke.vn.util.enums.flight.TripType;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import com.vietjoke.vn.util.enums.user.IdType;
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
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailedViewDTO  {
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

    private String promoCode;
    private String promoDescription;
    private BigDecimal promoDiscountAmount;

    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPhone;

    private List<PassengerDetailDTO> passengers = new ArrayList<>();

    private List<PaymentDetailDTO> payments = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerDetailDTO {
        private String firstName;
        private String lastName;
        private IdType idType;
        private PassengerType passengerType;
        private String idNumber;
        private List<FlightInfoDTO> flights = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightInfoDTO {
        private String flightNumber;
        private String flightRouteCode;
        private String flightDepartureAirport;
        private String flightArrivalAirport;
        private LocalDateTime flightDepartureTime;
        private LocalDateTime flightArrivalTime;
        private String flightGate;
        private String flightTerminal;
        private String airlineName;
        private String aircraftRegistrationNumber;
        private String aircraftModelCode;
        private String seatNumber;
        private BigDecimal totalAmount;
        private String fareName;
        private List<AddonDetailDTO> addons = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddonDetailDTO {
        private Long addonId;
        private String addonName;
        private AddonType addonTypeCode;
        private Integer quantity;
        private BigDecimal price;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetailDTO {
        private PaymentMethod paymentMethod;
        private String transactionId;
        private BigDecimal paymentAmount;
        private LocalDateTime paymentDate;
        private PaymentStatus paymentStatus;
    }
}