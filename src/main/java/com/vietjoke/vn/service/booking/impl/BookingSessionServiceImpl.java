package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.converter.BookingSessionConverter;
import com.vietjoke.vn.dto.booking.BookingSessionDTO;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.repository.booking.BookingSessionRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingSessionServiceImpl implements BookingSessionService {

    private final BookingSessionRepository bookingSessionRepository;

    private final BookingSessionConverter bookingSessionConverter;

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${session.ttl}")
    private Long sessionTimeToLive;

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
    public BookingSession updateBookingSession(BookingSession session) {
        return bookingSessionRepository.save(session);
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
        bookingSessionRepository.deleteById(sessionId);
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
    public void completeServiceSelection(String sessionToken) {
        BookingSession session = getSession(sessionToken);
        BookingSessionHelper.validateServiceBookingSteps(session);
        updateSessionStep(session);
    }

    private void resetSessionTTL(BookingSession session){
        session.setTimeToLive(sessionTimeToLive);
        for(String seatLockKey : session.getLockedSeats()){
            stringRedisTemplate.expire(seatLockKey, sessionTimeToLive, TimeUnit.SECONDS);
        }
    }
}
