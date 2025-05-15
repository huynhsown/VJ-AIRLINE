package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.converter.BookingSessionConverter;
import com.vietjoke.vn.dto.booking.BookingSessionDTO;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.pricing.PromoDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.repository.booking.BookingSessionRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.*;
import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingSessionServiceImpl implements BookingSessionService {

    @Autowired
    private BookingSessionRepository bookingSessionRepository;
    @Autowired
    private BookingSessionConverter bookingSessionConverter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    @Lazy
    private PriceCalculator priceCalculator;
    @Autowired
    private PromoValidator promoValidator;
    @Autowired
    private FlightProcessor flightProcessor;
    @Autowired
    @Lazy
    private PassengerProcessor passengerProcessor;

    @Value("${session.ttl}")
    private Long sessionTimeToLive;

    @Override
    @Transactional
    public BookingSession updateSelectedService(String sessionId, Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons) {
        BookingSession session = getSession(sessionId);
        session.setPassengerAddons(passengerAddons);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession createSession(SearchParamDTO searchCriteria) {
        BookingSession session = new BookingSession();
        session.setSearchCriteria(searchCriteria);
        session.setTimeToLive(sessionTimeToLive);
        session.setCurrentStep(BookingSessionStep.SELECT_FLIGHT);
        session.setNextStep(session.getCurrentStep().next());
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession getSession(String sessionId) {
        BookingSession session = bookingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionExpiredException("Session not found"));
        resetSessionTTL(session);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession updateSelectedFlight(String sessionId, SelectFlightRequestDTO selectedFlights) {
        BookingSession session = getSession(sessionId);
        session.setSelectedFlight(selectedFlights);
        updateSessionStep(session);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession updatePassengerInfo(String sessionId, PassengersInfoParamDTO passengerInfo) {
        BookingSession session = getSession(sessionId);
        session.setPassengersInfoParamDTO(passengerInfo);
        updateSessionStep(session);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession updateBookingSession(BookingSession session) {
        System.out.println(session.getPassengerAddons());
        BookingSession session1 = bookingSessionRepository.save(session);
        System.out.println(session1.getPassengerAddons());
        return session1;
    }

    @Override
    @Transactional
    public BookingSession addLocketSeat(String sessionId,String lockKey) {
        BookingSession session = getSession(sessionId);
        session.getLockedSeats().add(lockKey);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession removeLocketSeat(String sessionId, String lockKey) {
        BookingSession session = getSession(sessionId);
        session.getLockedSeats().remove(lockKey);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void deleteSession(String sessionId) {
        // Lấy thông tin session trước khi xóa
        BookingSession session = getSession(sessionId);

        // Lấy set các seat lock key
        Set<String> lockedSeats = session.getLockedSeats();

        // Debug: In ra số lượng seat cần xóa
        log.info("Number of seats to delete: {}", lockedSeats.size());
        log.info("Seats to delete: {}", lockedSeats);

        // Xóa các seat lock key trong Redis
        if (!lockedSeats.isEmpty()) {
            try {
                // Debug: Kiểm tra từng key có tồn tại trong Redis không
                for (String seatKey : lockedSeats) {
                    Boolean exists = stringRedisTemplate.hasKey(seatKey);
                    log.info("Key {} exists in Redis: {}", seatKey, exists);
                }

                // Xóa trực tiếp các key trong Redis
                Long deletedCount = stringRedisTemplate.delete(lockedSeats);
                log.info("Number of keys deleted from Redis: {}", deletedCount);

                // Debug: Kiểm tra lại sau khi xóa
                for (String seatKey : lockedSeats) {
                    Boolean stillExists = stringRedisTemplate.hasKey(seatKey);
                    log.info("Key {} still exists in Redis: {}", seatKey, stillExists);
                }
            } catch (Exception e) {
                log.error("Error removing locked seats from Redis: {}", e.getMessage());
                e.printStackTrace(); // In ra stack trace để debug
            } finally {
                // Xóa tất cả các key trong Set lockedSeats
                lockedSeats.clear();
                log.info("Cleared lockedSeats set");
            }
        }

        // Xóa session
        bookingSessionRepository.deleteById(sessionId);
        log.info("Deleted session with ID: {}", sessionId);
    }

    @Override
    @Transactional
    public ResponseDTO<BookingSessionDTO> getBookingSessionInfo(String sessionToken) {
        BookingSession session = getSession(sessionToken);
        BookingSessionDTO bookingSessionDTO = bookingSessionConverter.toBookingSessionDTO(session);
        return ResponseDTO.success(bookingSessionDTO);
    }

    @Override
    public void updateSessionStep(BookingSession session) {
        if(session.getNextStep() == null) return;
        session.setCurrentStep(session.getNextStep());
        session.setNextStep(session.getCurrentStep().next());
    }

    @Override
    @Transactional
    public ResponseDTO<?> completeServiceSelection(String sessionToken) {
        BookingSession session = getSession(sessionToken);
        BookingSessionHelper.validateServiceBookingSteps(session);
        updateSessionStep(session);
        session = bookingSessionRepository.save(session);
        return ResponseDTO.success(Map.of("sessionToken", session.getSessionId(),
                "currentStep", session.getCurrentStep(),
                "nextStep", session.getNextStep()));
    }

    @Override
    @Transactional
    public ResponseDTO<BookingPreviewDTO> getBookingPreview(String sessionToken) {
        BookingSession session = getSession(sessionToken);
        BookingSessionHelper.validatePreviewBookingSteps(session);

        List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerDetails =
                passengerProcessor.processPassengers(session);

        List<BookingPreviewDTO.FlightSummaryDTO> flightSummaries =
                flightProcessor.processFlights(session.getSelectedFlight(), passengerDetails);

        BigDecimal totalPrice = priceCalculator.calculateTotalPrice(flightSummaries);

        PromoDTO promoDTO = promoValidator.validateAndConvertPromo(
                session.getSearchCriteria().getCoupon(),
                session.getSearchCriteria().getTripPassengers(),
                totalPrice);

        BookingPreviewDTO preview = BookingPreviewDTO.builder()
                .sessionToken(session.getSessionId())
                .flights(flightSummaries)
                .passengerDetails(passengerDetails)
                .totalBookingPrice(totalPrice)
                .coupon(promoDTO)
                .build();

        return ResponseDTO.success(preview);
    }

    private void resetSessionTTL(BookingSession session){
        session.setTimeToLive(sessionTimeToLive);
        for(String seatLockKey : session.getLockedSeats()){
            stringRedisTemplate.expire(seatLockKey, sessionTimeToLive, TimeUnit.SECONDS);
        }
    }
}
