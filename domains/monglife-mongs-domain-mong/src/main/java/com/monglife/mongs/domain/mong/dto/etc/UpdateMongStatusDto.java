package com.monglife.mongs.domain.mong.dto.etc;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMongStatusDto {

    private Double changeExpValue;

    private Double changeWeightValue;

    private Double changeStrengthValue;

    private Double changeSatietyValue;

    private Double changeHealthyValue;

    private Double changeFatigueValue;

    private Integer changePoopCount;


    @Builder
    public UpdateMongStatusDto(Double ChangeExpValue, Double changeWeightValue, Double changeStrengthValue, Double changeSatietyValue, Double changeHealthyValue, Double changeFatigueValue, Integer changePoopCount) {
        this.changeExpValue = Optional.ofNullable(ChangeExpValue).orElse(0.0);
        this.changeWeightValue = Optional.ofNullable(changeWeightValue).orElse(0.0);
        this.changeStrengthValue = Optional.ofNullable(changeStrengthValue).orElse(0.0);
        this.changeSatietyValue = Optional.ofNullable(changeSatietyValue).orElse(0.0);
        this.changeHealthyValue = Optional.ofNullable(changeHealthyValue).orElse(0.0);
        this.changeFatigueValue = Optional.ofNullable(changeFatigueValue).orElse(0.0);
        this.changePoopCount = Optional.ofNullable(changePoopCount).orElse(0);
    }
}
