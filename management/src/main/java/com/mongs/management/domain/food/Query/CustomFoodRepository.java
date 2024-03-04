package com.mongs.management.domain.food.Query;

import com.mongs.management.domain.food.service.dto.FoodCategory;

import java.util.List;
import java.util.Map;

public interface CustomFoodRepository {
    Map<String,List<FoodCategory>> findFoodCategories();
    List<FoodCategory> foodList(String foodCode);

}