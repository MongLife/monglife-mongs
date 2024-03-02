package com.mongs.core.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "food_code")
public record FoodCode(
        @Id
        String code,
        String name,
        String groupCode,
        Integer fullness,
        Integer point
) {
}
