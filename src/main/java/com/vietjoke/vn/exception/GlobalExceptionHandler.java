package com.vietjoke.vn.exception;

import com.vietjoke.vn.constant.ErrorConstaints;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.exception.booking.MissingBookingStepException;
import com.vietjoke.vn.exception.chat.ChatSessionClosedException;
import com.vietjoke.vn.exception.chat.ChatSessionNotFoundException;
import com.vietjoke.vn.exception.data.DataNotFoundException;
import com.vietjoke.vn.exception.flight.FlightNotFoundException;
import com.vietjoke.vn.exception.flight.InvalidTripSelectionException;
import com.vietjoke.vn.exception.pricing.InvalidAddonQuantityException;
import com.vietjoke.vn.exception.pricing.SeatAlreadyReservedException;
import com.vietjoke.vn.exception.pricing.SeatSelectionNotAllowedException;
import com.vietjoke.vn.exception.session.SessionExpiredException;
import com.vietjoke.vn.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //DataException
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleDataNotFound(DataNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("data", ex.getMessage(), "error"))
        );
    }

    //SecurityException
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleBadCredentials(BadCredentialsException ex) {
        return new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("credentials", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(PermissionDenyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handlePermissionDeny(PermissionDenyException ex) {
        return new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("permission", ex.getMessage(), "error"))
        );
    }

    //UserException

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleDuplicateUsername(DuplicateUsernameException ex) {
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("username", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleDuplicateEmail(DuplicateEmailException ex) {
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("email", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleDuplicatePhone(DuplicatePhoneException ex) {
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("phone", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("user", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleAccountNotActive(AccountNotActivatedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of(new ErrorResponseDTO.ErrorDetail("account", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFoundException(NoHandlerFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "404");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponseDTO.ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDTO.ErrorDetail(
                        error.getField(),
                        error.getDefaultMessage(),
                        "validation"
                ))
                .collect(Collectors.toList());

        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors,
                ErrorConstaints.SESSION_EXPIRED
        );
    }

    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleSessionException(SessionExpiredException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Session expired or not found",
                List.of(new ErrorResponseDTO.ErrorDetail("session", "Session expired or not found", "error")),
                ErrorConstaints.SESSION_EXPIRED
        );
    }

    @ExceptionHandler(PassengerTypeRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handlePassengerTypeRequiredException(PassengerTypeRequiredException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                List.of(new ErrorResponseDTO.ErrorDetail("passengerType", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(PassengerInfomationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handlePassengerTypeRequiredException(PassengerInfomationException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                List.of(new ErrorResponseDTO.ErrorDetail("passengerType", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(MissingBookingStepException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    public ErrorResponseDTO handleMessingBookingStepException(MissingBookingStepException ex) {
        return new ErrorResponseDTO(
                HttpStatus.PRECONDITION_REQUIRED.value(),
                "Error",
                List.of(new ErrorResponseDTO.ErrorDetail("Booking", ex.getMessage(), "error")),
                ErrorConstaints.MISSING_BOOKING_STEP
        );
    }

    @ExceptionHandler(InvalidTripSelectionException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    public ErrorResponseDTO handleInvalidTripSelectionException(InvalidTripSelectionException ex) {
        return new ErrorResponseDTO(
                HttpStatus.PRECONDITION_REQUIRED.value(),
                "Error",
                List.of(new ErrorResponseDTO.ErrorDetail("Select Flight", ex.getMessage(), "error")),
                ErrorConstaints.ERROR_BOOKING_STEP
        );
    }

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleFlightNotFound(FlightNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Conflict",
                List.of(new ErrorResponseDTO.ErrorDetail("Search flight", ex.getMessage(), "error")),
                ErrorConstaints.ERROR_SEARCH
        );
    }

    @ExceptionHandler(SeatAlreadyReservedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleSeatAlreadyReserved(SeatAlreadyReservedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                List.of(new ErrorResponseDTO.ErrorDetail("Search flight", ex.getMessage(), "error")),
                ErrorConstaints.SEAT_ALREADY_RESERVED
        );
    }

    @ExceptionHandler(SeatSelectionNotAllowedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponseDTO handleSeatSelectionNotAllow(SeatSelectionNotAllowedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_ACCEPTABLE.value(),
                "Not Acceptable",
                List.of(new ErrorResponseDTO.ErrorDetail("Select Seat", ex.getMessage(), "error")),
                ErrorConstaints.SEAT_NOT_ALLOWED
        );
    }

    @ExceptionHandler(InvalidAddonQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleAddonQuantity(InvalidAddonQuantityException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                List.of(new ErrorResponseDTO.ErrorDetail("Select Addons", ex.getMessage(), "error")),
                ErrorConstaints.INVALID_ADDON_QUANTITY
        );
    }

    @ExceptionHandler(AccountAlreadyActivatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleAccountAlreadyActivated(AccountAlreadyActivatedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                List.of(new ErrorResponseDTO.ErrorDetail("Account", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(InvalidOtpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidOtp(InvalidOtpException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                List.of(new ErrorResponseDTO.ErrorDetail("OTP", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(OtpCooldownException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponseDTO handleOtpCooldown(OtpCooldownException ex) {
        return new ErrorResponseDTO(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                List.of(new ErrorResponseDTO.ErrorDetail("OTP", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(NoAvailableAdminException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponseDTO handleNoAvailableAdmin(NoAvailableAdminException ex) {
        return new ErrorResponseDTO(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                List.of(new ErrorResponseDTO.ErrorDetail("Admin", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(NotAnAdminException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleNotAnAdmin(NotAnAdminException ex) {
        return new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                List.of(new ErrorResponseDTO.ErrorDetail("Admin", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(UnauthorizedMessageException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleUnauthorizedMessage(UnauthorizedMessageException ex) {
        return new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Unauthorized Message",
                List.of(new ErrorResponseDTO.ErrorDetail("Message", ex.getMessage(), "error"))
        );
    }


    @ExceptionHandler(ChatSessionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleChatSessionNotFound(ChatSessionNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Chat Session Not Found",
                List.of(new ErrorResponseDTO.ErrorDetail("ChatSession", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(ChatSessionClosedException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponseDTO handleChatSessionClosed(ChatSessionClosedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.GONE.value(),
                "Chat Session Closed",
                List.of(new ErrorResponseDTO.ErrorDetail("ChatSession", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handlePasswordNotMatchException(PasswordNotMatchException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Password Not Match",
                List.of(new ErrorResponseDTO.ErrorDetail("Password", ex.getMessage(), "error"))
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleRoleNotFound(RoleNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Role Not Found",
                List.of(new ErrorResponseDTO.ErrorDetail("Role", ex.getMessage(), "error"))
        );
    }


}
