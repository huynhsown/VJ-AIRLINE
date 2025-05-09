package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.entity.booking.BookingStatusEntity;
import com.vietjoke.vn.util.enums.booking.BookingStatus;

public interface BookingStatusService {
    BookingStatusEntity getStatus(BookingStatus status);
}
