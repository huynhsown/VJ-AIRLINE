package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.AddonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddonRepository extends JpaRepository<AddonEntity, Long> {
    Page<AddonEntity> findByAddonTypeEntity_AddonCodeAndIsActive(String addonCode, Boolean isActive, Pageable pageable);
    Page<AddonEntity> findByAddonTypeEntity_AddonCode(String addonCode, Pageable pageable);
}
