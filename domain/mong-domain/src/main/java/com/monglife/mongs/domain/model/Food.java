package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Food {

    private String foodTypeCode;

    private String foodTypeName;

    private Integer price;

    private Boolean isCanBuy;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

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
