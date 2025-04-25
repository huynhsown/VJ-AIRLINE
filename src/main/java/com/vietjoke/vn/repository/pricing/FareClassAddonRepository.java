package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareClassAddonEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareClassAddonRepository extends JpaRepository<FareClassAddonEntity, Long> {
    boolean existsByAddonEntityAndFareClassEntity(AddonEntity addonEntity, FareClassEntity fareClassEntity);
}
