package com.vietjoke.vn.exception;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicateUsername(DuplicateUsernameException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<Map<String, Boolean>> handleAccountNotActive(AccountNotActivatedException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage(), Map.of("requires_activation", true));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseDTO.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleUserNotFound(BadCredentialsException ex) {
        return ResponseDTO.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    // Xử lý DuplicateEmailException
    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    // Xử lý DuplicatePhoneException
    @ExceptionHandler(DuplicatePhoneException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<String> handleDuplicatePhone(DuplicatePhoneException ex) {
        return ResponseDTO.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    // Xử lý PasswordNotMatchException
    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<String> handlePasswordNotMatch(PasswordNotMatchException ex) {
        return ResponseDTO.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    // Xử lý RoleNotFoundException
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO<String> handleRoleNotFound(RoleNotFoundException ex) {
        return ResponseDTO.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    // Xử lý PermissionDenyException
    @ExceptionHandler(PermissionDenyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<String> handlePermissionDeny(PermissionDenyException ex) {
        return ResponseDTO.error(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    // Xử lý lỗi validation từ @Valid
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

    // Xử lý mọi ngoại lệ khác (fallback)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<String> handleGeneralException(Exception ex) {
        try {
            return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống, thử lại sau");
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống", null);
        }
    }
}