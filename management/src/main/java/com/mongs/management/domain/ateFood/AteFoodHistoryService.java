package com.mongs.management.domain.ateFood;

import com.mongs.management.domain.food.service.dto.FoodCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AteFoodHistoryService {

    FoodCategory foodList(String foodCode, Long mongId);
}
