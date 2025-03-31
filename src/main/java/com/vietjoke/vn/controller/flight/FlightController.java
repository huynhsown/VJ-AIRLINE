package com.vietjoke.vn.controller.flight;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.SearchFlightResponseDTO;
import com.vietjoke.vn.service.flight.FlightService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/select-flight")
    public ResponseEntity<?> selectFlight(@Valid @RequestBody SearchParamDTO searchParamDTO) {
        ResponseDTO<SearchFlightResponseDTO> result = flightService.searchFlight(searchParamDTO);
        if(result.getStatus() == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

}
