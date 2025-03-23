package com.vietjoke.vn.dto.user;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String identifier;
    private String password;
}
