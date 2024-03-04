package com.mongs.management.domain.food.service;

import com.mongs.management.domain.food.service.dto.FoodCategory;

import java.util.List;
import java.util.Map;

public interface FoodService {

    Map<String,List<FoodCategory>> foodCategory();
    List<FoodCategory> foodList(String foodCode);
}
