package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.entity.booking.BookingSession;

import java.util.Map;

public interface BookingSessionService {
    BookingSession createSession(Map<String, Object> searchCriteria);
    BookingSession getSession(String sessionId);
    BookingSession updateSelectedFlight(String sessionId, Map<String, Object> selectedFlight);
    void deleteSession(String sessionId);
}
