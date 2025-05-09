package com.vietjoke.vn.service.booking;

import com.vietjoke.vn.dto.booking.BookingSessionDTO;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SelectFlightParamDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.entity.booking.BookingSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BookingSessionService {
    BookingSession updateSelectedService(String sessionId,
                                         Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons);
    BookingSession createSession(SearchParamDTO searchCriteria);
    BookingSession getSession(String sessionId);
    BookingSession updateSelectedFlight(String sessionId, SelectFlightRequestDTO selectedFlight);
    BookingSession updatePassengerInfo(String sessionId, PassengersInfoParamDTO passengerInfo);
    BookingSession updateBookingSession(BookingSession session);
    BookingSession addLocketSeat(String sessionId, String lockKey);
    BookingSession removeLocketSeat(String sessionId, String lockKey);
    void deleteSession(String sessionId);
    ResponseDTO<BookingSessionDTO> getBookingSessionInfo(String sessionToken);
    void updateSessionStep(BookingSession session);
    ResponseDTO<?> completeServiceSelection(String sessionToken);
    ResponseDTO<BookingPreviewDTO> getBookingPreview(String sessionToken);
}
