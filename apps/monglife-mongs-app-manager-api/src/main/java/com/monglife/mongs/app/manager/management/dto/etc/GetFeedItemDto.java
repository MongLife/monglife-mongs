package com.monglife.mongs.app.manager.management.dto.etc;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class GetFeedItemDto {

    private String foodTypeCode;

    private String foodTypeGroupCode;

    private String foodTypeName;

    private Integer price;

    private Boolean isCanBuy;

    private Double addWeightValue;

    private Double addStrengthValue;

    private Double addSatietyValue;

    private Double addHealthyValue;

    private Double addFatigueValue;


    @Builder
    public GetFeedItemDto(String foodTypeCode, String foodTypeGroupCode, String foodTypeName, Integer price, Boolean isCanBuy, Double addWeightValue, Double addStrengthValue, Double addSatietyValue, Double addHealthyValue, Double addFatigueValue) {
        this.foodTypeCode = foodTypeCode;
        this.foodTypeGroupCode = foodTypeGroupCode;
        this.foodTypeName = foodTypeName;
        this.price = price;
        this.isCanBuy = isCanBuy;
        this.addWeightValue = addWeightValue;
        this.addStrengthValue = addStrengthValue;
        this.addSatietyValue = addSatietyValue;
        this.addHealthyValue = addHealthyValue;
        this.addFatigueValue = addFatigueValue;
    }
}
