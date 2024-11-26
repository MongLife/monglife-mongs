package com.monglife.mongs.app.manager.management.dto.etc;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMongStatusDto {

    private Double addExpValue;

    private Double addWeightValue;

    private Double addStrengthValue;

    private Double addSatietyValue;

    private Double addHealthyValue;

    private Double addFatigueValue;

    private Integer addPoopCount;


    @Builder
    public UpdateMongStatusDto(Double addExpValue, Double addWeightValue, Double addStrengthValue, Double addSatietyValue, Double addHealthyValue, Double addFatigueValue, Integer addPoopCount) {
        this.addExpValue = Optional.ofNullable(addExpValue).orElse(0.0);
        this.addWeightValue = Optional.ofNullable(addWeightValue).orElse(0.0);
        this.addStrengthValue = Optional.ofNullable(addStrengthValue).orElse(0.0);
        this.addSatietyValue = Optional.ofNullable(addSatietyValue).orElse(0.0);
        this.addHealthyValue = Optional.ofNullable(addHealthyValue).orElse(0.0);
        this.addFatigueValue = Optional.ofNullable(addFatigueValue).orElse(0.0);
        this.addPoopCount = Optional.ofNullable(addPoopCount).orElse(0);
    }
}
