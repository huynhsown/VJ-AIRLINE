package com.vietjoke.vn.entity.location;

import com.vietjoke.vn.entity.core.BaseEntity;
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
