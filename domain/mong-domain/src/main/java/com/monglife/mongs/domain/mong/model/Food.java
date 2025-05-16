package com.monglife.mongs.domain.mong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Food {

    private final String foodTypeCode;

    private final String foodTypeName;

    private final Integer price;

    private final Boolean isCanBuy;

    private final Double weight;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    @Builder
    public Food(String foodTypeCode, String foodTypeName, Integer price, Boolean isCanBuy, Double weight, Double strength, Double satiety, Double healthy, Double fatigue) {
        this.foodTypeCode = foodTypeCode;
        this.foodTypeName = foodTypeName;
        this.price = price;
        this.isCanBuy = isCanBuy;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
    }
}
