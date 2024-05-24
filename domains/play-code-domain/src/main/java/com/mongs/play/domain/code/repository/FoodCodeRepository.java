package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.FoodCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodCodeRepository extends JpaRepository<FoodCode, String> {
    List<FoodCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
