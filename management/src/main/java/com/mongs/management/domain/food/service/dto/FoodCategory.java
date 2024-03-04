package com.mongs.management.domain.food.service.dto;

import java.time.LocalDateTime;

public record FoodCategory(
        String foodCode,
        String name,
        int price,
        LocalDateTime lastBuiedTime
) {
}
