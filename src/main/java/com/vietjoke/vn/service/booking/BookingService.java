package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO;
import com.vietjoke.vn.dto.booking.BookingHistoryDTO;
import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.user.UserEntity;

import java.util.List;

public interface BookingService {
    BookingEntity createBooking(String sessionToken, UserEntity userEntity, PayPalOrderDTO payPalOrderDTO);
    ResponseDTO<List<BookingHistoryDTO>> getBookingHistory(String username);
    ResponseDTO<BookingDetailedViewDTO> getBookingDetail(String username, String bookingReference);
}
