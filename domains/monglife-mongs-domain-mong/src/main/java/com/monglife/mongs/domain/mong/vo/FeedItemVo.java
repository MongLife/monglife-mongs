package com.monglife.mongs.domain.mong.vo;

import lombok.*;

@Getter
public class FeedItemVo {

    private final String foodTypeCode;

    private final String foodTypeGroupCode;

    private final String foodTypeName;

    private final Integer price;

    private final Boolean isCanBuy;

    private final Double addWeightValue;

    private final Double addStrengthValue;

    private final Double addSatietyValue;

    private final Double addHealthyValue;

    private final Double addFatigueValue;


    @Builder
    public FeedItemVo(String foodTypeCode, String foodTypeGroupCode, String foodTypeName, Integer price, Boolean isCanBuy, Double addWeightValue, Double addStrengthValue, Double addSatietyValue, Double addHealthyValue, Double addFatigueValue) {
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
