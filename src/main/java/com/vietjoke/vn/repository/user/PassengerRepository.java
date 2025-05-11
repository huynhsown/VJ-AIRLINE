package com.vietjoke.vn.repository.user;

import com.vietjoke.vn.entity.user.PassengerEntity;
import com.vietjoke.vn.util.enums.user.IdType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
    Optional<PassengerEntity> findByIdTypeAndIdNumber(IdType idType, String idNumber);
}
