//package com.vietjoke.vn.service.user.impl;
//
//import com.vietjoke.vn.entity.user.RoleEntity;
//import com.vietjoke.vn.entity.user.UserEntity;
//import com.vietjoke.vn.domain.user.exception.*;
//import com.vietjoke.vn.exception.RoleNotFoundException;
//import com.vietjoke.vn.repository.user.RoleRepository;
//import com.vietjoke.vn.repository.user.UserRepository;
//import com.vietjoke.vn.service.user.UserDomainService;
//import com.vietjoke.vn.domain.user.value.UserRegistrationData;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserDomainServiceImpl implements UserDomainService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserEntity register(UserRegistrationData userData) {
//
//        if(userRepository.existsByUsername(userData.getUsername())) {
//            throw new DuplicateUsernameException("Username already exists");
//        }
//
//        if(userRepository.existsByEmail(userData.getEmail())) {
//            throw new DuplicateEmailException("Email already exists");
//        }
//
//        if(userRepository.existsByUsername(userData.getPhone())) {
//            throw new DuplicatePhoneException("Phone already exists");
//        }
//
//        RoleEntity roleEntity = roleRepository.findByRoleCode(userData.getRoleCode())
//                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
//
//        if(roleEntity.getRoleCode().equals("ADMIN")) {
//            throw new PermissionDenyException("Username already exists");
//        }
//
//        String hashedPassword = passwordEncoder.encode(userData.getPassword());
//
//        return UserEntity.builder()
//                .username(userData.getUsername())
//                .email(userData.getEmail())
//                .phone(userData.getPhone())
//                .roleEntity(roleEntity)
//                .firstName(userData.getFirstName())
//                .lastName(userData.getLastName())
//                .dateOfBirth(userData.getDateOfBirth())
//                .address(userData.getAddress())
//                .passwordHash(hashedPassword)
//                .build();
//    }
//}
