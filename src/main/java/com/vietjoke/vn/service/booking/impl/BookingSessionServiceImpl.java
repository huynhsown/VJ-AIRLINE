package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.repository.booking.BookingSessionRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingSessionServiceImpl implements BookingSessionService {

    private final BookingSessionRepository bookingSessionRepository;

    @Override
    public BookingSession createSession(Map<String, Object> searchCriteria) {
        BookingSession session = new BookingSession();
        session.setSearchCriteria(searchCriteria);
        return session;
    }

    @Override
    public BookingSession getSession(String sessionId) {
        Optional<BookingSession> session = bookingSessionRepository.findById(sessionId);
        if(session.isEmpty()) {
            throw new SessionExpiredException("Session expired or not found")
        }
        return session.get();
    }

    @Override
    public BookingSession updateSelectedFlight(String sessionId, Map<String, Object> selectedFlight) {
        BookingSession session = getSession(sessionId);
        session.setSearchFlight(selectedFlight);
        return session;
    }

    @Override
    public void deleteSession(String sessionId) {
        bookingSessionRepository.deleteById(sessionId);
    }
}
