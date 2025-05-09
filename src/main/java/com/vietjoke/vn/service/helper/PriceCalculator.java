package com.vietjoke.vn.service.helper;

import com.vietjoke.vn.dto.pricing.PromoDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PriceCalculator {
    private final FlightProcessor flightProcessor;
    private final PassengerProcessor passengerProcessor;
    private final PromoValidator promoValidator;

    public BigDecimal calculateTotalPrice(List<BookingPreviewDTO.FlightSummaryDTO> flightSummaries) {
        return flightSummaries.stream()
                .map(this::calculateFlightTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateFlightTotal(BookingPreviewDTO.FlightSummaryDTO flight) {
        return Optional.ofNullable(flight.getTotalTicketPrice())
                .orElse(BigDecimal.ZERO)
                .add(Optional.ofNullable(flight.getTotalAddonPrice())
                        .orElse(BigDecimal.ZERO));
    }

    public BigDecimal calculatePaymentPrice(BookingSession session) {
        List<BookingPreviewDTO.FlightSummaryDTO> flightSummaries =
                flightProcessor.processFlights(session.getSelectedFlight(),
                        passengerProcessor.processPassengers(session));

        BigDecimal totalPrice = calculateTotalPrice(flightSummaries);

        PromoDTO promoDTO = promoValidator.validateAndConvertPromo(
                session.getSearchCriteria().getCoupon(),
                session.getSearchCriteria().getTripPassengers(),
                totalPrice);

        if (promoDTO != null && promoDTO.getIsAvailable()) {
            if (promoDTO.getIsPercentage()) {
                BigDecimal discountPercentage = promoDTO.getAmount()
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                BigDecimal discountAmount = totalPrice.multiply(discountPercentage);
                return totalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
            } else {
                return totalPrice.subtract(promoDTO.getAmount()).setScale(2, RoundingMode.HALF_UP);
            }
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateDiscountAmount(BookingSession session) {
        List<BookingPreviewDTO.FlightSummaryDTO> flightSummaries =
                flightProcessor.processFlights(session.getSelectedFlight(),
                        passengerProcessor.processPassengers(session));

        BigDecimal totalPrice = calculateTotalPrice(flightSummaries);

        PromoDTO promoDTO = promoValidator.validateAndConvertPromo(
                session.getSearchCriteria().getCoupon(),
                session.getSearchCriteria().getTripPassengers(),
                totalPrice);

        if (promoDTO == null || !promoDTO.getIsAvailable()) {
            return BigDecimal.ZERO;
        }

        if (promoDTO.getIsPercentage()) {
            BigDecimal discountPercentage = promoDTO.getAmount()
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            return totalPrice.multiply(discountPercentage)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            return promoDTO.getAmount().setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal calculateDiscountAmount(BigDecimal totalPrice, PromoDTO promoDTO) {
        if (promoDTO == null || !promoDTO.getIsAvailable()) {
            return BigDecimal.ZERO;
        }

        if (promoDTO.getIsPercentage()) {
            BigDecimal discountPercentage = promoDTO.getAmount()
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            return totalPrice.multiply(discountPercentage)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            return promoDTO.getAmount().setScale(2, RoundingMode.HALF_UP);
        }
    }
}