package com.vietjoke.vn;

import com.vietjoke.vn.domain.location.entity.CountryEntity;
import com.vietjoke.vn.entity.BookingDetail;
import com.vietjoke.vn.entity.SeatReservation;
import com.vietjoke.vn.repository.BookingDetailRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VietjokeAirlineApplicationTests {

    @Autowired
    BookingDetailRepo bookingDetailRepo;

    @Test
    void contextLoads() {
        SeatReservation seat = new SeatReservation();
        seat.setSeatNumber("12A");
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setBookingCode("1234a");
        bookingDetail.setSeatReservationEntity(seat);
        bookingDetailRepo.save(bookingDetail);
    }

    @Test
    void getBookingDetail() {
        bookingDetailRepo.deleteById(2L);
    }

    @Test
    void getSeatReservation() {
        
    }

}
