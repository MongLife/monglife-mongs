package com.mongs.core.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "food_code")
public record FoodCode(
        @Id
        String code,
        String name,
        String groupCode,
        Integer price,
        Double addWeightValue,
        Double addStrengthValue,
        Double addSatietyValue,
        Double addHealthyValue,
        Double addSleepValue,
        String buildVersion
){
}