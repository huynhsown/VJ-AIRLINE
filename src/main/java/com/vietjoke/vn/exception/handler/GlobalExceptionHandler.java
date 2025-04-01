package com.vietjoke.vn.exception.handler;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.data.DataNotFoundException;
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
    public ResponseDTO<String> handleDataNotFound(DataNotFoundException ex) {
        return ResponseDTO.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    //SecurityException
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDTO<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseDTO.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(PermissionDenyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<String> handlePermissionDeny(PermissionDenyException ex) {
        return ResponseDTO.error(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    //UserException

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicateUsername(DuplicateUsernameException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicatePhone(DuplicatePhoneException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseDTO.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<Map<String, Boolean>> handleAccountNotActive(AccountNotActivatedException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage(), Map.of("requires_activation", true));
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
    public ResponseDTO<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseDTO.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
    }

    // Xử lý lỗi không mong muốn (fallback)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<String> handleGeneralException(Exception ex) {
        return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống, thử lại sau");
    }
}
