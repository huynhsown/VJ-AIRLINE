package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.location.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/airports")
    public ResponseEntity<?> getAllAirports(){
        ResponseDTO<List<AirportDTO>> responseDTO = airportService.getAllAirports();
        return ResponseEntity.ok(responseDTO);
    }

}
