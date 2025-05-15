package com.vietjoke.vn.service.user;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.*;
import com.vietjoke.vn.entity.user.UserEntity;

import java.util.Map;

public interface UserService {
    ResponseDTO<String> register(UserRegisterRequestDTO user);
    ResponseDTO<Map<String, String>> login(UserLoginRequestDTO user);
    UserEntity getUserByUsername(String username);
    UserEntity getAdminByUsername(String username);
    UserEntity getUserByEmail(String email);
    void addBookingToUser(String username, String sessionToken);
    ResponseDTO<String> verifyOTP(VerifyOtpRequestDTO verifyOtp);
    ResponseDTO<Map<String, String>> resendOTP(String email);
    ResponseDTO<Map<String, String>> sendResetPasswordOTP(String email);
    ResponseDTO<UserProfileResponseDTO> getUserProfile(String username);
    ResponseDTO<UserProfileResponseDTO> updateProfile(String username, UserUpdateRequestDTO request);
    ResponseDTO<UserProfileResponseDTO> updateAvatar(String username, UserUpdateAvatarRequestDTO request);
}
