package com.vietjoke.vn.exception.handler;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.user.PermissionDenyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

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
}
