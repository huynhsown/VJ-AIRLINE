package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.repository.booking.BookingSessionRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingSessionServiceImpl implements BookingSessionService {

    private final BookingSessionRepository bookingSessionRepository;

    @Value("${session.ttl}")
    private Long sessionTimeToLive;

    @Override
    @Transactional
    public BookingSession createSession(SearchParamDTO searchCriteria) {
        BookingSession session = new BookingSession();
        session.setSearchCriteria(searchCriteria);
        session.setTimeToLive(sessionTimeToLive);
        log.info("Creating session with ID: {}, TTL: {}", session.getSessionId(), session.getTimeToLive());
        BookingSession savedSession = bookingSessionRepository.save(session);
        log.info("Session saved with ID: {}", savedSession.getSessionId());
        return savedSession;
    }

    @Override
    @Transactional
    public BookingSession getSession(String sessionId) {
        BookingSession session = bookingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionExpiredException("Session not found"));
        session.setTimeToLive(sessionTimeToLive);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession updateSelectedFlight(String sessionId, SelectFlightRequestDTO selectedFlights) {
        BookingSession session = getSession(sessionId);
        session.setSelectedFlight(selectedFlights);
        return bookingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public BookingSession updatePassengerInfo(String sessionId, PassengersInfoParamDTO passengerInfo) {
        BookingSession session = getSession(sessionId);
        session.setPassengersInfoParamDTO(passengerInfo);
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
}
