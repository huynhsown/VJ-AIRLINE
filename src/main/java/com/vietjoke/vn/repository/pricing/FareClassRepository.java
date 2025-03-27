package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.FareClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareClassRepository extends JpaRepository<FareClassEntity, Long> {
    boolean existsByCode(String code);
}
