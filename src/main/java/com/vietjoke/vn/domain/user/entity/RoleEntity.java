package com.vietjoke.vn.domain.user.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String roleCode;

    @Column(nullable = false)
    private String roleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> userEntities;
}
