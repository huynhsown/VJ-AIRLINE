package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.booking.BookingStatusEntity;
import com.vietjoke.vn.entity.pricing.PaymentMethodEntity;
import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.booking.BookingStatusService;
import com.vietjoke.vn.service.helper.PriceCalculator;
import com.vietjoke.vn.service.pricing.PromoCodeService;
import com.vietjoke.vn.service.user.UserService;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import com.vietjoke.vn.util.enums.flight.TripType;
import com.vietjoke.vn.util.paypal.PayPalUtil;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingSessionService bookingSessionService;
    private final PayPalUtil payPalUtil;
    private final UserService userService;
    private final PriceCalculator priceCalculator;
    private final BookingStatusService bookingStatusService;
    private final PromoCodeService promoCodeService;


    @Override
    public BookingEntity createBooking(String sessionToken, Map<String, String> orderDetail, String username) {
        BookingSession session = bookingSessionService.getSession(sessionToken);

        SearchParamDTO searchParamDTO = session.getSearchCriteria();
        UserEntity userEntity = userService.getUserByUsername(username);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String reference = "VN" + uuid.substring(0, 6).toUpperCase();

        BigDecimal totalAmount = priceCalculator.calculatePaymentPrice(session);
        BigDecimal discountAmount = priceCalculator.calculateDiscountAmount(session);

        int adultCount = searchParamDTO.getTripPassengersAdult();
        int childCount = searchParamDTO.getTripPassengersChild();
        int infantCount = searchParamDTO.getTripPassengersInfant();
        TripType tripType = TripType.valueOf(searchParamDTO.getTripType());
        BookingStatusEntity bookingStatus = bookingStatusService.getStatus(BookingStatus.COMPLETED);
        PromoCodeEntity promoCodeEntity = promoCodeService.getPromoCode(searchParamDTO.getCoupon());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookingReference(reference);
        bookingEntity.setUserEntity(userEntity);
        bookingEntity.setTotalAmount(totalAmount);
        bookingEntity.setDiscountAmount(discountAmount);
        bookingEntity.setAdultCount(adultCount);
        bookingEntity.setChildCount(childCount);
        bookingEntity.setInfantCount(infantCount);
        bookingEntity.setTripType(tripType);
        bookingEntity.setBookingStatusEntity(bookingStatus);
        bookingEntity.setPromoCodeEntity(promoCodeEntity);



        return bookingEntity;
    }
}
