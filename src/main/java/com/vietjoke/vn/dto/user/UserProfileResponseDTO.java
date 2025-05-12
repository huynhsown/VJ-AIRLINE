package com.vietjoke.vn.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileResponseDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private String avatarUrl;
    private Boolean emailVerified;
    private Boolean isActive;
    private String roleCode;
}