package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.AddonTypeEntity;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddonTypeRepository extends JpaRepository<AddonTypeEntity, Long> {

    Optional<AddonTypeEntity> findByAddonCode(String addonCode);

}
