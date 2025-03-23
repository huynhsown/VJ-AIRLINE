package com.vietjoke.vn.dto.user;

import com.vietjoke.vn.dto.booking.BookingDetailDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.util.enums.user.Gender;
import com.vietjoke.vn.util.enums.user.IdType;
import com.vietjoke.vn.util.enums.user.PassengerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PassengerDTO extends BaseDTO {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private IdType idType;
    private String idNumber;
    private String phone;
    private String email;
    private PassengerType passengerType;
    private List<BookingDetailDTO> bookingDetails;
}