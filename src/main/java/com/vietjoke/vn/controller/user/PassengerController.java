package com.vietjoke.vn.controller.user;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.service.user.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/booking/passenger-info")
    public ResponseEntity<?> inputPassengerInfo(@Valid @RequestBody PassengersInfoParamDTO infoParamDTO){
        return ResponseEntity.ok(passengerService.inputPassengerInfo(infoParamDTO));
    }

    @GetMapping("/booking/passenger-info")
    public ResponseEntity<?> getPassengerInfo(@Valid @RequestBody SessionTokenRequestDTO sessionToken){
        return ResponseEntity.ok(passengerService.getPassengerInfo(sessionToken));
    }

}
