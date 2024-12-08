package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.type.FoodTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodTypeRepository extends JpaRepository<FoodTypeEntity, Long> {

    Optional<FoodTypeEntity> findByFoodCodeComnCode(String foodCode);

    List<FoodTypeEntity> findByFoodCodeGroupCode(String foodCodeGroupCode);
}
