package com.vietjoke.vn.controller.user;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.*;
import com.vietjoke.vn.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequestDTO user) {
        ResponseDTO<String> responseDTO = userService.register(user);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDTO user) {
        ResponseDTO<Map<String, String>> responseDTO = userService.login(user);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOtpRequestDTO verifyOtp) {
        return ResponseEntity.ok(userService.verifyOTP(verifyOtp));
    }

    @PostMapping("/auth/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.resendOTP(email));
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute UserUpdateAvatarRequestDTO request) {
        return ResponseEntity.ok(userService.updateAvatar(userDetails.getUsername(), request));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

}
