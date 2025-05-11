package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.user.UserEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface BookingService {
    BookingEntity createBooking(String sessionToken, UserEntity userEntity, PayPalOrderDTO payPalOrderDTO);
}
