package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoRepository extends JpaRepository<PromoCodeEntity, Long> {
    Optional<PromoCodeEntity> findByCode(String promoCode);
}
