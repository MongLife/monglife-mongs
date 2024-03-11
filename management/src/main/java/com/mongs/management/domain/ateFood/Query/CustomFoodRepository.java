package com.mongs.management.domain.ateFood.Query;

import com.mongs.core.code.entity.FoodCode;
import com.mongs.management.domain.food.service.dto.FoodCategory;

import java.util.List;
import java.util.Map;

public interface CustomFoodRepository {
    FoodCategory foodList(FoodCode foodCode);
}