package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.converter.BookingConverter;
import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO;
import com.vietjoke.vn.dto.booking.BookingHistoryDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.entity.booking.*;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
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
import com.vietjoke.vn.service.user.UserService;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import com.vietjoke.vn.util.enums.flight.TripType;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import com.vietjoke.vn.util.paypal.PayPalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final FareAvailabilityService fareAvailabilityService;
    private final UserService userService;
    private final BookingConverter bookingConverter;
    private final PayPalUtil payPalUtil;


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


    @Override
    public ResponseDTO<List<BookingHistoryDTO>> getBookingHistory(String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        List<BookingEntity> bookingEntities = userEntity.getBookingEntities();
        return ResponseDTO.success(bookingEntities.stream()
                .sorted(Comparator.comparing(BookingEntity::getModifiedDate).reversed())
                .map(bookingConverter::toBookingHistoryDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseDTO<BookingDetailedViewDTO> getBookingDetail(String username, String bookingReference) {
        UserEntity userEntity = userService.getUserByUsername(username);
        BookingEntity bookingEntity = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        BookingDetailedViewDTO detailDTO = bookingConverter.toBookingDetailedViewDTO(bookingEntity);
        return ResponseDTO.success(detailDTO);
    }

    @Override
    @Transactional
    public ResponseDTO<?> cancelBooking(String username, String bookingReference) {
        BookingEntity bookingEntity = getBooking(username, bookingReference);
        BookingStatusEntity bookingStatus = bookingStatusService.getStatus(BookingStatus.CANCELLED);
        bookingEntity.setBookingStatusEntity(bookingStatus);
        List<BookingDetailEntity> bookingDetailEntities = bookingEntity.getBookingDetailEntities();
        List<FlightEntity> sortedFlights = bookingDetailEntities.stream()
                .map(BookingDetailEntity::getFlightEntity)
                .sorted(Comparator.comparing(FlightEntity::getScheduledDeparture))
                .toList();
        if(sortedFlights.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        if(LocalDateTime.now().isAfter(sortedFlights.get(0).getScheduledDeparture())) {
            throw new RuntimeException("Flight not scheduled");
        }
        //Refund
        Map<FareClassEntity, List<BookingDetailEntity>> fareMap = bookingDetailEntities
                .stream()
                .filter(detail -> detail.getFareClassEntity() != null)
                .collect(Collectors.groupingBy(BookingDetailEntity::getFareClassEntity));

        BigDecimal discountEachBooking = bookingEntity.getDiscountAmount()
                .divide(BigDecimal.valueOf(bookingDetailEntities.size()), 2, RoundingMode.HALF_UP);

        BigDecimal refundFee = fareMap.entrySet().stream()
                .filter(entry -> entry.getKey().getRefundAllowed())
                .flatMap(entry -> entry.getValue().stream()
                        .map(detail -> detail.getTotalAmount()
                                .subtract(discountEachBooking)
                                .subtract(BigDecimal.valueOf(entry.getKey().getRefundFee()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Set seat available
        bookingDetailEntities.forEach(
                detail -> {
                    SeatReservationEntity seatEntity = detail.getSeatReservationEntity();
                    if (seatEntity != null) {
                        seatEntity.setSeatStatus(SeatStatus.AVAILABLE);
                        seatReservationService.save(seatEntity);
                        detail.setSeatReservationEntity(null);
                        FlightEntity flightEntity = seatEntity.getFlightEntity();
                        FareClassEntity fareClassEntity = seatEntity.getFareClassEntity();
                        FareAvailabilityEntity fareAvailability = fareAvailabilityService.
                                increaseAvailableSeat(flightEntity.getFlightNumber(), fareClassEntity.getCode());
                    }
                    detail.setSeatReservationEntity(null);
                }
        );

        List<BookingPaymentEntity> bookingPaymentEntities = bookingEntity.getBookingPaymentEntities();

        try {
            bookingRepository.save(bookingEntity);
            if(!bookingPaymentEntities.isEmpty()) {
                Map<String, String> refundResult = payPalUtil.refundOrder(
                        bookingPaymentEntities.get(0).getTransactionId(), refundFee
                );
                return ResponseDTO.success(Map.of("Booking cancelled", refundResult));
            }
            return ResponseDTO.success("Booking cancelled");
        } catch (Exception ignored) {

        }
        return ResponseDTO.success("Booking cancelled");
    }

    @Override
    public BookingEntity getBooking(String username, String bookingReference) {
        UserEntity userEntity = userService.getUserByUsername(username);
        return bookingRepository.findByUserEntityAndBookingReference(userEntity, bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
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

                FareAvailabilityEntity fareAvailabilityEntity = fareAvailabilityService.decreaseAvailableSeat(
                        flightDetail.getFlightNumber(),
                        flightDetail.getFareCode()
                );

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
