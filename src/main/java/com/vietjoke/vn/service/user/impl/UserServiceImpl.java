package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.converter.UserConverter;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.user.*;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.user.*;
import com.vietjoke.vn.repository.user.RoleRepository;
import com.vietjoke.vn.repository.user.UserRepository;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.cloudinary.CloudinaryService;
import com.vietjoke.vn.service.user.UserService;
import com.vietjoke.vn.util.JwtTokenUtil;
import com.vietjoke.vn.util.PasswordGenerator;
import com.vietjoke.vn.util.enums.user.OTPType;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.ResourceTransactionManager;

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
    private final OTPService otpService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final PasswordGenerator passwordGenerator;

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
        String otp = otpService.generateAndSaveOtp(userEntity.getEmail());
        emailService.sendRegistrationEmail(
                userEntity.getEmail(),
                userEntity.getLastName() + ' ' + userEntity.getFirstName(),
                otp
        );
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
    public UserEntity getAdminByUsername(String username) {
        UserEntity userEntity = getUserByUsername(username);
        if (!"ADMIN".equals(userEntity.getRoleEntity().getRoleCode())) {
            throw new NotAnAdminException("");
        }
        return userEntity;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
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

    @Override
    @Transactional
    public ResponseDTO<String> verifyOTP(VerifyOtpRequestDTO verifyOtp) {
        UserEntity userEntity = getUserByEmail(verifyOtp.getEmail());
        if (userEntity.getIsActive()) {
            throw new AccountAlreadyActivatedException("Account is already activated");
        }
        if(!otpService.validateOtp(verifyOtp.getEmail(), verifyOtp.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if(verifyOtp.getOtpType().equals(OTPType.VERIFY)){
            userEntity.setIsActive(true);
            userRepository.save(userEntity);
        }
        else{
            String password = PasswordGenerator.generatePassword(8);
            String hashedPassword = passwordEncoder.encode(password);
            userEntity.setPasswordHash(hashedPassword);
            emailService.sendForgotPasswordOTP(userEntity.getEmail(),
                    userEntity.getLastName() + ' ' + userEntity.getFirstName(),
                    hashedPassword);
        }
        return ResponseDTO.success("Verification successful");
    }

    @Override
    public ResponseDTO<Map<String, String>> resendOTP(String email) {
        UserEntity userEntity = getUserByEmail(email);
        if (userEntity.getIsActive()) {
            throw new AccountAlreadyActivatedException("Account is already activated");
        }
        String otp = otpService.generateAndSaveOtp(userEntity.getEmail());
        emailService.sendRegistrationEmail(
                userEntity.getEmail(),
                userEntity.getLastName() + ' ' + userEntity.getFirstName(),
                otp
        );
        return ResponseDTO.success(Map.of(
                "status", HttpStatus.CREATED.toString(),
                "message", "OTP resent successfully"
                ));
    }

    @Override
    public ResponseDTO<Map<String, String>> sendResetPasswordOTP(String email) {
        UserEntity userEntity = getUserByEmail(email);
        if (userEntity.getIsActive()) {
            throw new AccountAlreadyActivatedException("Account is already activated");
        }
        String otp = otpService.generateAndSaveOtp(userEntity.getEmail());
        emailService.sendForgotPasswordOTP(
                userEntity.getEmail(),
                userEntity.getLastName() + ' ' + userEntity.getFirstName(),
                otp
        );
        return ResponseDTO.success(Map.of(
                "status", HttpStatus.CREATED.toString(),
                "message", "OTP resent successfully"
        ));
    }

    @Override
    public ResponseDTO<UserProfileResponseDTO> getUserProfile(String username) {
        UserEntity userEntity = getUserByUsername(username);
        UserProfileResponseDTO profile = userConverter.toUserProfileResponseDTO(userEntity);
        return ResponseDTO.success(profile);
    }

    @Override
    @Transactional
    public ResponseDTO<UserProfileResponseDTO> updateProfile(String username, UserUpdateRequestDTO request) {
        UserEntity userEntity = getUserByUsername(username);
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setPhone(request.getPhone());
        userEntity.setDateOfBirth(request.getDateOfBirth());
        userEntity.setAddress(request.getAddress());

        if (request.isChangingPassword()) {
            if (request.getPreviousPassword() == null) {
                throw new ValidationException("Previous password is required to change password");
            }

            if (!passwordEncoder.matches(request.getPreviousPassword(), userEntity.getPasswordHash())) {
                throw new ValidationException("Previous password is incorrect");
            }

            if (!request.isPasswordMatching()) {
                throw new ValidationException("New password and confirmation do not match");
            }

            userEntity.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        return ResponseDTO.success(userConverter.toUserProfileResponseDTO(userEntity));
    }

    @Override
    public ResponseDTO<UserProfileResponseDTO> updateAvatar(String username, UserUpdateAvatarRequestDTO request) {
        UserEntity user = getUserByUsername(username);

        String url = cloudinaryService.uploadImage(request.getAvatar());
        user.setAvatarUrl(url);
        userRepository.save(user);
        return ResponseDTO.success(userConverter.toUserProfileResponseDTO(user));
    }
}
