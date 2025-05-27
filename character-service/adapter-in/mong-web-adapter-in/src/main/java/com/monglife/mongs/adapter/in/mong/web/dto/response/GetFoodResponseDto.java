package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetFoodResponseDto {
    
    private String foodCode;

    private String foodName;

    private Integer price;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Boolean isCanBuy;

    @Builder
    public GetFoodResponseDto(String foodCode, String foodName, Integer price, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Boolean isCanBuy) {
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.price = price;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.isCanBuy = isCanBuy;
    }
}
