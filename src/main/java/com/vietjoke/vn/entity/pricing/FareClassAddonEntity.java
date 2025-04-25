package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fare_class_included_addons")
@Getter
@Setter
public class FareClassAddonEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "fare_class_id", nullable = false)
    private FareClassEntity fareClassEntity;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private AddonEntity addonEntity;

    @Enumerated(EnumType.STRING)
    private AddonType addonType;
}