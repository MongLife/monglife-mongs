package com.mongs.management.domain.food.service;

import com.mongs.management.domain.ateFood.AteFoodHistory;
import com.mongs.management.domain.ateFood.AteFoodHistoryRepository;
import com.mongs.management.domain.food.FoodRepository;
import com.mongs.management.domain.food.service.dto.FoodCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{

    private final FoodRepository foodRepository;
    @Override
    public Map<String, List<FoodCategory>> foodCategory() {
        return foodRepository.findFoodCategories();
    }

    @Override
    public List<FoodCategory> foodList(String foodCode) {
        return foodRepository.foodList(foodCode);
    }
}
