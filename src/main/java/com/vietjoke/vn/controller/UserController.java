package com.vietjoke.vn.controller;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.UserLoginRequestDTO;
import com.vietjoke.vn.dto.user.UserRegisterRequestDTO;
import com.vietjoke.vn.exception.user.DuplicateUsernameException;
import com.vietjoke.vn.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequestDTO user) {
        ResponseDTO<String> responseDTO = userService.register(user);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDTO user) {
        ResponseDTO<Map<String, String>> responseDTO = userService.login(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Dit me tuyet voi");
    }
}
