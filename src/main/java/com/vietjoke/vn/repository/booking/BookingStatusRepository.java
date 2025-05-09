package com.vietjoke.vn.repository.booking;

import com.vietjoke.vn.entity.booking.BookingStatusEntity;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingStatusRepository extends JpaRepository<BookingStatusEntity, Long> {
    Optional<BookingStatusEntity> findByStatusCode(BookingStatus statusCode);
}
