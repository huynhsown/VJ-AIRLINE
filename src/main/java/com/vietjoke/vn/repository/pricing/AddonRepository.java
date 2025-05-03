package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddonRepository extends JpaRepository<AddonEntity, Long> {
    Page<AddonEntity> findByAddonTypeEntity_AddonCodeAndIsActive(AddonType addonCode, Boolean isActive, Pageable pageable);
    Page<AddonEntity> findByAddonTypeEntity_AddonCode(AddonType addonCode, Pageable pageable);
    Page<AddonEntity> findByAddonTypeEntity_AddonCodeAndIsActiveAndIsFree(AddonType addonCode,
                                                                          Boolean isActive,
                                                                          boolean isFree,
                                                                          Pageable pageable
                                                                          );
    List<AddonEntity> findByNameIn(List<String> names);
}
