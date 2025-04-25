package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.pricing.FareClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FareClassRepository extends JpaRepository<FareClassEntity, Long> {
    boolean existsByCode(String code);
    Optional<FareClassEntity> findByCode(String code);
    List<FareClassEntity> findByMealIncluded(boolean mealIncluded);
}
