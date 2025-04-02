package com.monglife.mongs.client.manager.vo;

import com.monglife.mongs.client.manager.dto.response.GetMinimalMongResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongVo {

    /**
     * Mong Basic
     */
    private final Long mongId;

    private final String mongName;

    private final String mongTypeCode;

    /**
     * Mong Status
     */
    private final Double weight;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    private final Integer poopCount;

    @Builder
    public MongVo(Long mongId, String mongName, String mongTypeCode, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer poopCount) {
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

    public static MongVo of(GetMinimalMongResponseDto getMinimalMongResponseDto) {

        return MongVo.builder()
                .mongId(getMinimalMongResponseDto.getMongId())
                .mongName(getMinimalMongResponseDto.getMongName())
                .mongTypeCode(getMinimalMongResponseDto.getMongTypeCode())
                .weight(getMinimalMongResponseDto.getWeight())
                .strength(getMinimalMongResponseDto.getStrength())
                .satiety(getMinimalMongResponseDto.getSatiety())
                .healthy(getMinimalMongResponseDto.getHealthy())
                .fatigue(getMinimalMongResponseDto.getFatigue())
                .poopCount(getMinimalMongResponseDto.getPoopCount())
                .build();
    }
}
