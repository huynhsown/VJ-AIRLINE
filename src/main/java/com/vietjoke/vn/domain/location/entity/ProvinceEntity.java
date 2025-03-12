package com.vietjoke.vn.domain.location.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "provinces")
public class ProvinceEntity extends BaseEntity {

    private String provinceCode;
    private String provinceName;
    private String provinceEngName;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity countryEntity;

    @OneToMany(mappedBy = "provinceEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AirportEntity> airportEntities;

}
