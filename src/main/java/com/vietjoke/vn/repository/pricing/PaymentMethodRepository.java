package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
}
