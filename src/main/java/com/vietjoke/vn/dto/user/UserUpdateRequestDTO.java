package com.vietjoke.vn.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequestDTO {
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Pattern(
            regexp = "^((\\+84)|0)(3[2-9]|5[2-9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$",
            message = "Invalid Vietnamese phone number format"
    )
    private String phone;

    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    // Chỉ validate khi có thay đổi mật khẩu
    private String previousPassword;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one number, and one special character"
    )
    private String password;

    private String confirmPassword;

    // Thêm các phương thức kiểm tra
    public boolean isChangingPassword() {
        return password != null && !password.isEmpty();
    }

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}