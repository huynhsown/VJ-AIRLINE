package com.vietjoke.vn.controller.flight;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SelectFlightParamDTO;
import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.SearchFlightResponseDTO;
import com.vietjoke.vn.service.flight.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/flight/search")
    public ResponseEntity<?> searchFlight(@Valid @RequestBody SearchParamDTO searchParamDTO) {
        ResponseDTO<SearchFlightResponseDTO> result = flightService.searchFlight(searchParamDTO);
        if(result.getStatus() == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/flight/select")
    public ResponseEntity<?> selectFlight(@Valid @RequestBody SelectFlightRequestDTO selection){
        return ResponseEntity.ok(flightService.selectFlight(selection));
    }


    @GetMapping("/booking/get-seats")
    public ResponseEntity<?> getSeatsByToken(@Valid @RequestBody SessionTokenRequestDTO token){
        return ResponseEntity.ok(flightService.getSeatOfFlight(token));
    }
}
