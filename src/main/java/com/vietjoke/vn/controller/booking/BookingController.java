package com.vietjoke.vn.controller.booking;

import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingSessionService bookingSessionService;
    private final BookingService bookingService;

    @GetMapping("/booking/flight-info")
    public ResponseEntity<?> getBookingFlightInfo(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingSessionInfo(sessionToken));
    }

    @PostMapping("/booking/service/complete")
    public ResponseEntity<?> completeServiceSelection(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.completeServiceSelection(sessionToken));
    }

    @GetMapping("/booking/preview")
    public ResponseEntity<?> getBookingPreview(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingPreview(sessionToken));
    }

    @GetMapping("/booking/history")
    public ResponseEntity<?> getBookingHistory(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.getBookingHistory(username));
    }

    @GetMapping("/booking/detail")
    public ResponseEntity<?> getBookingDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String bookingReference) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.getBookingDetail(username, bookingReference));
    }

    @PostMapping("/booking/cancel")
    public ResponseEntity<?> cancelBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String bookingReference) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.cancelBooking(username, bookingReference));
    }
}
