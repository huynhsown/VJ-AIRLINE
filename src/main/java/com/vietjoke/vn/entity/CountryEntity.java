package com.vietjoke.vn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity extends BaseEntity {
    private String countryCode;
    private String countryName;
}
