package com.vietjoke.vn.controller.booking;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.cloudinary.CloudinaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingSessionService bookingSessionService;

    @GetMapping("/booking/flight-info")
    public ResponseEntity<?> getBookingFlightInfo(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingSessionInfo(sessionToken));
    }

    @PostMapping("/booking/service/complete")
    public ResponseEntity<?> completeServiceSelection(@RequestParam String sessionToken) {
        bookingSessionService.completeServiceSelection(sessionToken);
        return ResponseEntity.ok(ResponseDTO.success("Service selection completed"));
    }

}
