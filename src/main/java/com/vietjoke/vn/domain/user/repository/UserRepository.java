package com.vietjoke.vn.domain.user.repository;

import com.vietjoke.vn.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameOrPhoneOrEmail(String userCode, String phone, String email);
}
