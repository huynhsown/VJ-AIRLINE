package com.vietjoke.vn.repository.booking;

import com.vietjoke.vn.entity.booking.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
