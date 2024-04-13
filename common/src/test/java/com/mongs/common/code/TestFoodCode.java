package com.mongs.common.code;

public enum TestFoodCode {

    STAR_CANDY("별사탕","FD000", "FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    APPLE("사과", "FD010","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    KIM_BAB("삼각김밥", "FD011","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    SANDWICH("샌드위치" ,"FD012","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    PIZZA("피자","FD020","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    CHICKEN("치킨","FD021","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    STAKE("스테이크","FD022","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    IN_SAM("인삼","FD030","FD", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    CHOCOLATE("초콜릿","SN000","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    CANDY("사탕","SN001","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    COLA("콜라","SN002","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    COOKIE("쿠키","SN010","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    CAKE("케이크","SN011","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    FRY("감자튀김","SN012","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15),
    ICE_CREAM("아이스크림","SN013","SN", 0, 10D, 10D, 10D, 10D, 10D, 60 * 15)
    ;

    private final String name;
    private final String code;
    private final String groupCode;
    private final Integer price;
    private final Double addWeightValue;
    private final Double addStrengthValue;
    private final Double addSatietyValue;
    private final Double addHealthyValue;
    private final Double addSleepValue;
    private final Integer delaySeconds;

    TestFoodCode(String name, String code, String groupCode, Integer price, Double addWeightValue, Double addStrengthValue, Double addSatietyValue, Double addHealthyValue, Double addSleepValue, Integer delaySeconds) {
        this.name = name;
        this.code = code;
        this.groupCode = groupCode;
        this.price = price;
        this.addWeightValue = addWeightValue;
        this.addStrengthValue = addStrengthValue;
        this.addSatietyValue = addSatietyValue;
        this.addHealthyValue = addHealthyValue;
        this.addSleepValue = addSleepValue;
        this.delaySeconds = delaySeconds;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public Integer getPrice() {
        return price;
    }

    public Double getAddWeightValue() {
        return addWeightValue;
    }

    public Double getAddStrengthValue() {
        return addStrengthValue;
    }

    public Double getAddSatietyValue() {
        return addSatietyValue;
    }

    public Double getAddHealthyValue() {
        return addHealthyValue;
    }

    public Double getAddSleepValue() {
        return addSleepValue;
    }

    public Integer getDelaySeconds() {
        return delaySeconds;
    }
}
