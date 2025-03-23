package com.vietjoke.vn.entity.booking;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "booking_addons")
public class BookingAddonEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "booking_detail_id", nullable = false)
    private BookingDetailEntity bookingDetailEntity;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private AddonEntity addonEntity;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
}
