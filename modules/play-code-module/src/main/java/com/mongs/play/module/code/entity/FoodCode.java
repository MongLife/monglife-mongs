package com.mongs.play.module.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
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
        Integer delaySeconds,
        String buildVersion
){
}