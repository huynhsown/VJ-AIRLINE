package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.pricing.PaymentMethodCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private PaymentMethodCode code;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // e.g., ZaloPay, VNPAY

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
}
