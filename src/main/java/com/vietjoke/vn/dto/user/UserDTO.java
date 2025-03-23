package com.vietjoke.vn.dto.user;

import com.vietjoke.vn.dto.booking.BookingDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO {
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private Boolean emailVerified;
    private Boolean isActive;
    private RoleDTO role;
    private List<BookingDTO> bookings;
}