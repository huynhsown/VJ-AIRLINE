package com.vietjoke.vn.controller.booking;

import com.vietjoke.vn.service.booking.BookingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        return ResponseEntity.ok(bookingSessionService.completeServiceSelection(sessionToken));
    }

    @GetMapping("/booking/preview") // Ở bước preview
    public ResponseEntity<?> getBookingPreview(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingPreview(sessionToken));
    }

}
