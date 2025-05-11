package com.vietjoke.vn.service.user;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.UserLoginRequestDTO;
import com.vietjoke.vn.dto.user.UserRegisterRequestDTO;
import com.vietjoke.vn.entity.user.UserEntity;

import java.util.Map;

public interface UserService {
    ResponseDTO<String> register(UserRegisterRequestDTO user);
    ResponseDTO<Map<String, String>> login(UserLoginRequestDTO user);
    UserEntity getUserByUsername(String username);
    void addBookingToUser(String username, String sessionToken);
}
