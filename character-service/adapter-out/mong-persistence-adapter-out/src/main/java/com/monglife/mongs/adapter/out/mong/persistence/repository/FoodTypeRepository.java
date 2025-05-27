package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodTypeRepository extends JpaRepository<FoodEntity, Long> {

    Optional<FoodEntity> findByComnCode(String foodCode);
}
