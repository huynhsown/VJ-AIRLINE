package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.converter.BookingPreviewConverter;
import com.vietjoke.vn.converter.BookingSessionConverter;
import com.vietjoke.vn.dto.booking.BookingSessionDTO;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.repository.booking.BookingSessionRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    private FareClassService fareClassService;
    @Autowired
    private BookingPreviewConverter bookingPreviewConverter;

    @Autowired
    @Lazy
    private FlightService flightService;
    @Autowired
    @Lazy
    private FareAvailabilityService fareAvailabilityService;
    @Autowired
    @Lazy
    private AddonService addonService;

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

        List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerBookingDetailDTOS = processPassengerList(session);
        List<BookingPreviewDTO.FlightSummaryDTO> flightSummaryDTOS = processFlightList(
                session, passengerBookingDetailDTOS);

        BigDecimal totalBookingPrice = flightSummaryDTOS.stream()
                .map(f -> Optional.ofNullable(f.getTotalTicketPrice())
                        .orElse(BigDecimal.ZERO)
                        .add(Optional.ofNullable(f.getTotalAddonPrice())
                                .orElse(BigDecimal.ZERO))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BookingPreviewDTO bookingPreviewDTO = BookingPreviewDTO
                .builder()
                .sessionToken(session.getSessionId())
                .flights(flightSummaryDTOS)
                .passengerDetails(passengerBookingDetailDTOS)
                .totalBookingPrice(totalBookingPrice)
                .build();

        return ResponseDTO.success(bookingPreviewDTO);
    }

    private List<BookingPreviewDTO.FlightSummaryDTO> processFlightList(
            BookingSession session,
            List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerBookingDetailDTOS
    ) {
        return session.getSelectedFlight().getFlights()
                .stream()
                .map(SelectFlightDTO::getFlightNumber)
                .distinct()
                .map(flightNumber -> {
                    List<BookingPreviewDTO.PassengerFlightDetailDTO> matchingFlightDetails =
                            passengerBookingDetailDTOS.stream()
                                    .flatMap(p -> p.getPassengerFlightDetailDTOS().stream())
                                    .filter(f -> flightNumber.equals(f.getFlightNumber()))
                                    .toList();

                    BigDecimal totalTicketPrice = matchingFlightDetails.stream()
                            .map(f -> Optional.ofNullable(f.getTicketPrice()).orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalAddonPrice = matchingFlightDetails.stream()
                            .map(f -> Optional.ofNullable(f.getAddonPrice()).orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return BookingPreviewDTO.FlightSummaryDTO.builder()
                            .flightNumber(flightNumber)
                            .totalTicketPrice(totalTicketPrice)
                            .totalAddonPrice(totalAddonPrice)
                            .build();
                })
                .toList();
    }

    private List<BookingPreviewDTO.PassengerBookingDetailDTO> processPassengerList(BookingSession session) {
        PassengersInfoParamDTO infoParamDTO = session.getPassengersInfoParamDTO();
        List<PassengerDTO> passengerDTOS = BookingSessionHelper.getAllPassengers(infoParamDTO);
        List<SelectFlightDTO> selectFlightDTOS = session.getSelectedFlight().getFlights();

        Map<String, FlightEntity> flightCache = new HashMap<>();
        Map<String, FareClassEntity> fareClassCache = new HashMap<>();
        Map<String, FareAvailabilityEntity> fareAvailabilityCache = new HashMap<>();

        for (SelectFlightDTO selectFlightDTO : selectFlightDTOS) {
            String flightNumber = selectFlightDTO.getFlightNumber();
            String fareCode = selectFlightDTO.getFareCode();

            FlightEntity flightEntity = flightCache.computeIfAbsent(flightNumber,
                    flightService::getFlightByFlightNumber);

            FareClassEntity fareClassEntity = fareClassCache.computeIfAbsent(fareCode,
                    fareClassService::getFareClass);

            String cacheKey = flightNumber + "_" + fareCode;
            fareAvailabilityCache.computeIfAbsent(cacheKey,
                    k -> fareAvailabilityService.getFareAvailability(flightEntity, fareClassEntity));
        }

        List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerBookingDetailDTOS = new ArrayList<>();

        for (PassengerDTO passengerDTO : passengerDTOS) {
            List<BookingPreviewDTO.PassengerFlightDetailDTO> passengerFlightDetailDTOS = new ArrayList<>();

            for (SelectFlightDTO selectFlightDTO : selectFlightDTOS) {
                String flightNumber = selectFlightDTO.getFlightNumber();
                String fareCode = selectFlightDTO.getFareCode();

                FlightEntity flightEntity = flightCache.get(flightNumber);
                FareAvailabilityEntity fareAvailabilityEntity = fareAvailabilityCache.get(flightNumber + "_" + fareCode);

                List<AddonSelectionDTO> addons = Optional.ofNullable(session.getPassengerAddons())
                        .map(map -> map.get(passengerDTO.getUuid()))
                        .map(flightMap -> flightMap.get(flightNumber))
                        .orElse(Collections.emptyList());

                List<BookingPreviewDTO.AddonDetailDTO> addonDetailDTOS = addons.stream()
                        .map(addon -> {
                            AddonEntity addonEntity = addonService.getAddonById(addon.getAddonId());
                            return bookingPreviewConverter.toAddonDetailDTO(addonEntity, addon);
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                BookingPreviewDTO.PassengerFlightDetailDTO passengerFlightDetailDTO =
                        bookingPreviewConverter.toPassengerFlightDetailDTO(
                                flightEntity,
                                fareAvailabilityEntity,
                                addonDetailDTOS);

                passengerFlightDetailDTOS.add(passengerFlightDetailDTO);
            }

            BookingPreviewDTO.PassengerBookingDetailDTO passengerBookingDetailDTO =
                    bookingPreviewConverter.toPassengerBookingDetailDTO(passengerDTO, passengerFlightDetailDTOS);

            passengerBookingDetailDTOS.add(passengerBookingDetailDTO);
        }

        return passengerBookingDetailDTOS;
    }


    private void resetSessionTTL(BookingSession session){
        session.setTimeToLive(sessionTimeToLive);
        for(String seatLockKey : session.getLockedSeats()){
            stringRedisTemplate.expire(seatLockKey, sessionTimeToLive, TimeUnit.SECONDS);
        }
    }
}
