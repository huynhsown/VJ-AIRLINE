package com.vietjoke.vn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "Thành công", data);
    }

    public static <T> ResponseDTO<T> error(int status, String message) {
        return new ResponseDTO<>(status, message, null);
    }

    public static <T> ResponseDTO<T> error(int status, String message, T data) {
        return new ResponseDTO<>(status, message, data);
    }
}
