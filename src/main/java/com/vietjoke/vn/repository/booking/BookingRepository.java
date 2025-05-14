package com.vietjoke.vn.repository.booking;

import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Optional<BookingEntity> findByBookingReference(String bookingReference);
    Optional<BookingEntity> findByUserEntityAndBookingReference(UserEntity userEntity, String bookingReference);
}
