package com.mongs.management.domain.food;

import com.mongs.management.domain.food.Query.CustomFoodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>, CustomFoodRepository {

    @Query("select f from Food as f where f.foodCode = :foodCode")
    Food findFood (@Param("foodCode") String foodCode);
}
