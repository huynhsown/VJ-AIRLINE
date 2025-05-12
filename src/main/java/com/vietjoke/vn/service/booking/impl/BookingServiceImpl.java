package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.entity.booking.*;
import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.repository.booking.BookingRepository;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.booking.BookingStatusService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.helper.PassengerProcessor;
import com.vietjoke.vn.service.helper.PriceCalculator;
import com.vietjoke.vn.service.pricing.*;
import com.vietjoke.vn.service.user.PassengerService;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import com.vietjoke.vn.util.enums.flight.TripType;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingSessionService bookingSessionService;
    private final PriceCalculator priceCalculator;
    private final BookingStatusService bookingStatusService;
    private final PromoCodeService promoCodeService;
    private final PassengerProcessor passengerProcessor;
    private final FlightService flightService;
    private final PassengerService passengerService;
    private final FareClassService fareClassService;
    private final SeatRedisService seatRedisService;
    private final SeatReservationService seatReservationService;
    private final AddonService addonService;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public BookingEntity createBooking(String sessionToken, UserEntity userEntity, PayPalOrderDTO payPalOrderDTO) {
        BookingSession session = bookingSessionService.getSession(sessionToken);

        SearchParamDTO searchParamDTO = session.getSearchCriteria();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String reference = "VN" + uuid.substring(0, 6).toUpperCase();

        BigDecimal totalAmount = priceCalculator.calculateTotalPrice(session);
        BigDecimal discountAmount = priceCalculator.calculateDiscountAmount(session);

        int adultCount = searchParamDTO.getTripPassengersAdult();
        int childCount = searchParamDTO.getTripPassengersChild();
        int infantCount = searchParamDTO.getTripPassengersInfant();
        TripType tripType = TripType.fromValue(searchParamDTO.getTripType());
        BookingStatusEntity bookingStatus = bookingStatusService.getStatus(BookingStatus.COMPLETED);
        PromoCodeEntity promoCodeEntity = promoCodeService.getPromoCode(searchParamDTO.getCoupon());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookingReference(reference);
        bookingEntity.setUserEntity(userEntity);
        bookingEntity.setTotalAmount(totalAmount);
        bookingEntity.setDiscountAmount(discountAmount);
        bookingEntity.setBookingPaymentEntities(List.of(createBookingPayments(
                bookingEntity, payPalOrderDTO
        )));
        bookingEntity.setAdultCount(adultCount);
        bookingEntity.setChildCount(childCount);
        bookingEntity.setInfantCount(infantCount);
        bookingEntity.setTripType(tripType);
        bookingEntity.setBookingStatusEntity(bookingStatus);
        bookingEntity.setPromoCodeEntity(promoCodeEntity);
        bookingEntity.setBookingDetailEntities(createBookingDetails(
                session,
                bookingEntity
        ));
        return bookingRepository.save(bookingEntity);
    }

    private BookingPaymentEntity createBookingPayments(
            BookingEntity bookingEntity,
            PayPalOrderDTO payPalOrderDTO) {
        BookingPaymentEntity bookingPaymentEntity = new BookingPaymentEntity();
        bookingPaymentEntity.setBookingEntity(bookingEntity);
        bookingPaymentEntity.setPaymentAmount(payPalOrderDTO.getCapture().getAmount());
        bookingPaymentEntity.setPaymentMethod(payPalOrderDTO.getPaymentMethod());
        bookingPaymentEntity.setTransactionId(payPalOrderDTO.getCapture().getCaptureId());
        if (bookingPaymentEntity.getPaymentAmount().compareTo(
                bookingEntity.getTotalAmount().subtract(bookingEntity.getDiscountAmount())) >= 0) {
            bookingPaymentEntity.setPaymentStatus(PaymentStatus.COMPLETED);
        }
        else{
            bookingPaymentEntity.setPaymentStatus(PaymentStatus.FAILED);
        }
        return bookingPaymentEntity;
    }

    private List<BookingDetailEntity> createBookingDetails(BookingSession session, BookingEntity bookingEntity) {
        List<BookingDetailEntity> bookingDetails = new ArrayList<>();
        List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerDetails =
                passengerProcessor.processPassengers(session);
        for (BookingPreviewDTO.PassengerBookingDetailDTO passengerDetail : passengerDetails) {
            for (BookingPreviewDTO.PassengerFlightDetailDTO flightDetail : passengerDetail.getPassengerFlightDetailDTOS()) {
                BookingDetailEntity bookingDetail = new BookingDetailEntity();

                bookingDetail.setBookingEntity(bookingEntity);
                bookingDetail.setFlightEntity(flightService.getFlightByFlightNumber(flightDetail.getFlightNumber()));
                bookingDetail.setPassengerEntity(passengerService.getOrCreatePassenger(passengerDetail));
                bookingDetail.setFareClassEntity(fareClassService.getFareClass(
                        flightDetail.getFareCode()
                ));
                bookingDetail.setFareAmount(flightDetail.getTicketPrice());
                BigDecimal discountAmount = BigDecimal.ZERO;
                bookingDetail.setDiscountAmount(discountAmount);
                BigDecimal taxAmount = BigDecimal.ZERO;
                BigDecimal feeAmount = BigDecimal.ZERO;
                bookingDetail.setTaxAmount(taxAmount);
                bookingDetail.setFeeAmount(feeAmount);
                BigDecimal totalAmount = flightDetail.getTicketPrice()
                        .add(flightDetail.getAddonPrice())
                        .add(taxAmount)
                        .add(feeAmount);
                bookingDetail.setTotalAmount(totalAmount);

                String seatNumber = seatRedisService.getSeatNumber(
                        flightDetail.getFlightNumber(),
                        passengerDetail.getPassengerUuid());

                SeatReservationEntity seatEntity = seatReservationService.getOrAssignSeat(
                        flightDetail.getFlightNumber(),
                        flightDetail.getFareCode(),
                        seatNumber
                );

                seatEntity.setSeatStatus(SeatStatus.RESERVED);
                bookingDetail.setSeatReservationEntity(seatEntity);

                List<BookingAddonEntity> bookingAddons = new ArrayList<>();
                for (BookingPreviewDTO.AddonDetailDTO addonDetail : flightDetail.getAddons()) {
                    BookingAddonEntity bookingAddon = new BookingAddonEntity();
                    bookingAddon.setBookingDetailEntity(bookingDetail);
                    bookingAddon.setAddonEntity(addonService.getAddonById(addonDetail.getAddonId()));
                    bookingAddon.setQuantity(addonDetail.getQuantity());
                    bookingAddon.setPrice(addonDetail.getPrice());
                    bookingAddons.add(bookingAddon);
                }
                bookingDetail.setBookingAddonEntities(bookingAddons);
                bookingDetails.add(bookingDetail);
            }
        }
        return bookingDetails;
    }
}
