package com.vietjoke.vn.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

    @NotEmpty(message = "Username/Email/Phone number are required")
    private String identifier;

    @NotEmpty(message = "Password is required")
    private String password;
}
