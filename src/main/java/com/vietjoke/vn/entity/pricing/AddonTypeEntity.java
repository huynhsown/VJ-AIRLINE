package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "addon_type")
public class AddonTypeEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private AddonType addonCode;

    private String addonName;

    @OneToMany(mappedBy = "addonTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddonEntity> addonEntities;
}
