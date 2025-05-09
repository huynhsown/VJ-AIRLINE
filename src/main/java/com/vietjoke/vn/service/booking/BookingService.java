package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.entity.booking.BookingEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface BookingService {
    BookingEntity createBooking(String sessionToken, Map<String, String> orderDetail, String username);
}
