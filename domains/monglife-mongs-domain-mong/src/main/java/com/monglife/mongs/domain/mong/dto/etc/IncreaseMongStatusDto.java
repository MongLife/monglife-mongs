package com.monglife.mongs.domain.mong.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncreaseMongStatusDto {

    private Double exp;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Integer poopCount;

    @Builder
    public IncreaseMongStatusDto(Double exp, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer poopCount) {
        this.exp = exp;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.poopCount = poopCount;
    }
}
