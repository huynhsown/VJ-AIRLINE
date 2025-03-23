package com.vietjoke.vn.entity.location;

import com.vietjoke.vn.entity.core.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String countryCode;

    @Column(nullable = false)
    private String countryName;

    private String countryEngName;

    @Column(nullable = false, unique = true)
    private String areaCode;

    @OneToMany(mappedBy = "countryEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ProvinceEntity> provinceEntities;
}
