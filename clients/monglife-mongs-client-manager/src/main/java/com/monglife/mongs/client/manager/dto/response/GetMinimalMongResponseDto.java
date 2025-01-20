package com.monglife.mongs.client.manager.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMinimalMongResponseDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Integer poopCount;

    @Builder
    public GetMinimalMongResponseDto(Long mongId, String mongName, String mongTypeCode, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer poopCount) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.poopCount = poopCount;
    }
}
