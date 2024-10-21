package com.monglife.mongs.app.management.repository;

import com.monglife.mongs.app.management.domain.FoodCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCodeRepository extends JpaRepository<FoodCodeEntity, String> {
}
