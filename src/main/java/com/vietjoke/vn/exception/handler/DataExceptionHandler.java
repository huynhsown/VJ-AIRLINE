package com.vietjoke.vn.exception.handler;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.data.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO<String> handleDataNotFound(DataNotFoundException ex) {
        return ResponseDTO.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
