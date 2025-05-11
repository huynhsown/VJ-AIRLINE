package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.converter.UserConverter;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.UserLoginRequestDTO;
import com.vietjoke.vn.dto.user.UserRegisterRequestDTO;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.user.*;
import com.vietjoke.vn.repository.user.RoleRepository;
import com.vietjoke.vn.repository.user.UserRepository;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.user.UserService;
import com.vietjoke.vn.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final BookingService bookingService;

    @Override
    public ResponseDTO<String> register(UserRegisterRequestDTO user) {

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException("Username is already exists");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email is already exists");
        }

        if(userRepository.existsByPhone(user.getUsername())) {
            throw new DuplicatePhoneException("Username is already exists");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())) {
            throw new PasswordNotMatchException("Password is not match");
        }

        RoleEntity roleEntity = roleRepository.findByRoleCode(user.getRoleCode())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        if(roleEntity.getRoleCode().equals("ADMIN")) {
            throw new PermissionDenyException("Admin role not found");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = userConverter.toUserEntity(user);
        userEntity.setPasswordHash(hashedPassword);
        userEntity.setRoleEntity(roleEntity);
        userRepository.save(userEntity);

        return ResponseDTO.success("Success");
    }

    @Override
    public ResponseDTO<Map<String, String>> login(UserLoginRequestDTO user) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsernameOrPhoneOrEmail(user.getIdentifier(),
                user.getIdentifier(),
                user.getIdentifier());
        if(optionalUserEntity.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        UserEntity existingUser = optionalUserEntity.get();

        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPasswordHash())) {
            throw new BadCredentialsException("Wrong password");
        }

        if(!existingUser.getIsActive()){
            throw new AccountNotActivatedException("Account not activated");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getIdentifier(), user.getPassword(),
                existingUser.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtil.generateToken(existingUser);
        return ResponseDTO.success(Map.of("token", "Bearer "+ token));
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void addBookingToUser(String username, String sessionToken) {
//        UserEntity userEntity = getUserByUsername(username);
//        BookingEntity bookingEntity = bookingService.createBooking(
//                sessionToken,
//                userEntity
//        );
//        userEntity.getBookingEntities().add(bookingEntity);
//        userRepository.save(userEntity);
    }
}
