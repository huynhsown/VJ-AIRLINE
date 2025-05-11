package com.vietjoke.vn.repository.location;

import com.vietjoke.vn.entity.location.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    boolean existsByCountryCode(String countryCode);
    Optional<CountryEntity> findByCountryCode(String countryCode);
}
