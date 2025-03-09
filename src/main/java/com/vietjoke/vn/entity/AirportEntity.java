package com.vietjoke.vn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "airports")
public class AirportEntity extends BaseEntity{

    private String airportCode;
    private String airportName;
    private String airportEngName;
    private String timeZone;
    private String utcOffset;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private ProvinceEntity provinceEntity;

    @OneToMany(mappedBy = "originAirportEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<RouteEntity> routeOriginEntities;

    @OneToMany(mappedBy = "destinationAirportEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<RouteEntity> routeDestinationEntities;
}
