package com.vietjoke.vn.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vietjoke.vn.dto.booking.BookingDetailDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.util.enums.user.Gender;
import com.vietjoke.vn.util.enums.user.IdType;
import com.vietjoke.vn.util.enums.user.PassengerType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PassengerDTO {
    private String uuid;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Passenger type is required")
    private PassengerType passengerType;

    private String countryCode;
    private IdType idType;
    private String idNumber;
    private String phone;

    private String accompanyingAdultFirstName;
    private String accompanyingAdultLastName;

    @Email
    private String email;

    public PassengerDTO() {
        this.uuid = UUID.randomUUID().toString();
    }
}