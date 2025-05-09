package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.entity.booking.BookingStatusEntity;
import com.vietjoke.vn.repository.booking.BookingStatusRepository;
import com.vietjoke.vn.service.booking.BookingStatusService;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingStatusServiceImpl implements BookingStatusService {
    private final BookingStatusRepository bookingStatusRepository;

    @Override
    public BookingStatusEntity getStatus(BookingStatus status) {
        return bookingStatusRepository.findByStatusCode(status)
                .orElseThrow(() -> new RuntimeException("Booking Status not found"));
    }
}
