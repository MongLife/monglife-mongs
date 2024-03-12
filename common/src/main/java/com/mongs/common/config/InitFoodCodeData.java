package com.mongs.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitFoodCodeData {
    STAR_CANDY("별사탕","FD000", "FD", 0, 0D, 0D, 0D, 0D, 0D),
    APPLE("사과", "FD010","FD", 0, 0D, 0D, 0D, 0D, 0D),
    KIM_BAB("삼각김밥", "FD011","FD", 0, 0D, 0D, 0D, 0D, 0D),
    SANDWICH("샌드위치" ,"FD012","FD", 0, 0D, 0D, 0D, 0D, 0D),
    PIZZA("피자","FD020","FD", 0, 0D, 0D, 0D, 0D, 0D),
    CHICKEN("치킨","FD021","FD", 0, 0D, 0D, 0D, 0D, 0D),
    STAKE("스테이크","FD022","FD", 0, 0D, 0D, 0D, 0D, 0D),
    IN_SAM("인삼","FD030","FD", 0, 0D, 0D, 0D, 0D, 0D),
    CHOCOLATE("초콜릿","SN000","SN", 0, 0D, 0D, 0D, 0D, 0D),
    CANDY("사탕","SN001","SN", 0, 0D, 0D, 0D, 0D, 0D),
    COLA("콜라","SN002","SN", 0, 0D, 0D, 0D, 0D, 0D),
    COOKIE("쿠키","SN010","SN", 0, 0D, 0D, 0D, 0D, 0D),
    CAKE("케이크","SN011","SN", 0, 0D, 0D, 0D, 0D, 0D),
    FRY("감자튀김","SN012","SN", 0, 0D, 0D, 0D, 0D, 0D),
    ICE_CREAM("아이스크림","SN013","SN", 0, 0D, 0D, 0D, 0D, 0D)
    ;

    private final String name;
    private final String code;
    private final String groupCode;
    private final Integer point;
    private final Double addWeightValue;
    private final Double addStrengthValue;
    private final Double addSatietyValue;
    private final Double addHealthyValue;
    private final Double addSleepValue;
}
