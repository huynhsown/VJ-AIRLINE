package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SelectFlightParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.entity.booking.BookingSession;

import java.util.List;
import java.util.Map;

public interface BookingSessionService {
    BookingSession createSession(SearchParamDTO searchCriteria);
    BookingSession getSession(String sessionId);
    BookingSession updateSelectedFlight(String sessionId, SelectFlightRequestDTO selectedFlight);
    BookingSession updatePassengerInfo(String sessionId, PassengersInfoParamDTO passengerInfo);
    BookingSession addLocketSeat(String sessionId, String lockKey);
    BookingSession removeLocketSeat(String sessionId, String lockKey);
    void deleteSession(String sessionId);


}
