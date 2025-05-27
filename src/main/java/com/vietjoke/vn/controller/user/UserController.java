package com.vietjoke.vn.controller.user;

import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.*;
import com.vietjoke.vn.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "User registration",
            description = "Register a new user with username, email, phone, password, and role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - password not match or role not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - permission denied for certain roles",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - duplicate username, email or phone",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequestDTO user) {
        ResponseDTO<String> responseDTO = userService.register(user);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user by username/phone/email and password, returns JWT token if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns JWT token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - wrong password",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account not activated",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDTO user) {
        ResponseDTO<Map<String, String>> responseDTO = userService.login(user);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Verify OTP for account activation or password reset",
            description = "Verify the OTP sent to user's email. OTP type can be VERIFY (activate account) or RESET (reset password)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OTP verified successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid OTP or Account already activated",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account not activated (when resetting password)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/auth/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOtpRequestDTO verifyOtp) {
        return ResponseEntity.ok(userService.verifyOTP(verifyOtp));
    }

    @Operation(
            summary = "Resend OTP to user email",
            description = "Resend OTP for account activation if the account is not activated yet."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "OTP resent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Account is already activated",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/auth/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.resendOTP(email));
    }

    @Operation(
            summary = "Send OTP for resetting password",
            description = "Send an OTP to the user's email for password reset. The account must be activated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OTP sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is not activated",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendResetPasswordOTP(email));
    }

    @Operation(
            summary = "Update user avatar",
            description = "Upload a new avatar image for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Avatar updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute UserUpdateAvatarRequestDTO request) {
        return ResponseEntity.ok(userService.updateAvatar(userDetails.getUsername(), request));
    }

    @Operation(summary = "Get user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @Operation(
            summary = "Update user profile",
            description = "Update the authenticated user's profile information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for the update request",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/user/update-profile")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                               @Valid @RequestBody UserUpdateRequestDTO request) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.updateProfile(username, request));
    }
}
