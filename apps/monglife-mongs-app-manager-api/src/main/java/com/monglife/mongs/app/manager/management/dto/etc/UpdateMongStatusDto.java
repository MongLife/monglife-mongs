package com.monglife.mongs.app.manager.management.dto.etc;

import lombok.*;

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
        this.addExpValue = addExpValue == null ? 0.0 : addExpValue;
        this.addWeightValue = addWeightValue == null ? 0.0 : addWeightValue;
        this.addStrengthValue = addStrengthValue == null ? 0.0 : addStrengthValue;
        this.addSatietyValue = addSatietyValue == null ? 0.0 : addSatietyValue;
        this.addHealthyValue = addHealthyValue == null ? 0.0 : addHealthyValue;
        this.addFatigueValue = addFatigueValue == null ? 0.0 : addFatigueValue;
        this.addPoopCount = addPoopCount == null ? 0 : addPoopCount;
    }
}
