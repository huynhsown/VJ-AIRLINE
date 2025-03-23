package com.vietjoke.vn.repository.user;

import com.vietjoke.vn.entity.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByRoleCode(String name);
    Optional<RoleEntity> findByRoleCode(String roleCode);
}
