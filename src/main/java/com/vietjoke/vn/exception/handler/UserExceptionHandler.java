package com.vietjoke.vn.exception.handler;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class UserExceptionHandler {

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
}
