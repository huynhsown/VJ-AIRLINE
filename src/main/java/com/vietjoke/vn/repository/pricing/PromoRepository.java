package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<PromoCodeEntity, Long> {
}
