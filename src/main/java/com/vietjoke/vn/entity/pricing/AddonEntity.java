package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.booking.BookingAddonEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.pricing.Currency;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "addons")
public class AddonEntity extends BaseEntity {

    @Column(name = "addon_code", length = 20, nullable = false, unique = true)
    private String addonCode;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", length = 10, nullable = false)
    private Currency currency = Currency.VND;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "addon_type", length = 20)
    private AddonType addonType;

    @Column(name = "img_url", length = 255)
    private String imgUrl;

    @OneToMany(mappedBy = "addonEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BookingAddonEntity> bookingAddonEntities;
}
