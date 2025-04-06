package com.vietjoke.vn.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionTokenRequestDTO {
    @NotBlank(message = "Session Token is required")
    private String sessionToken;
}
