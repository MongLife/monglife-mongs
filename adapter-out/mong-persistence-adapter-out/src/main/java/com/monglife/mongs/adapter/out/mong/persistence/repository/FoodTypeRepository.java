package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.FoodTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodTypeRepository extends JpaRepository<FoodTypeEntity, Long> {

    Optional<FoodTypeEntity> findByComnCode(String foodCode);

    List<FoodTypeEntity> findByComnGroupCode(String foodCodeGroupCode);
}
