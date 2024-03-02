package com.mongs.management.domain.mong.service.dto;

import java.time.LocalDateTime;

public record FoodList (
        String name,
        String foodCode,
        int price,
        LocalDateTime lastBuy
){
}
