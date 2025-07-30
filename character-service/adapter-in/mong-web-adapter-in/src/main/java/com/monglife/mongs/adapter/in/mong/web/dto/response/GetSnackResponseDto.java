package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetSnackResponseDto {

    private String snackCode;

    private String snackName;

    private Integer price;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Boolean isCanBuy;

    @Builder
    public GetSnackResponseDto(String snackCode, String snackName, Integer price, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Boolean isCanBuy) {
        this.snackCode = snackCode;
        this.snackName = snackName;
        this.price = price;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.isCanBuy = isCanBuy;
    }
}
