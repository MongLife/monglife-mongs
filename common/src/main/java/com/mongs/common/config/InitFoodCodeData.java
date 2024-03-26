package com.mongs.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitFoodCodeData {
    STAR_CANDY("별사탕","FD000", "FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD000"),
    APPLE("사과", "FD010","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD010"),
    KIM_BAB("삼각김밥", "FD011","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD011"),
    SANDWICH("샌드위치" ,"FD012","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD002"),
    PIZZA("피자","FD020","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD020"),
    CHICKEN("치킨","FD021","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD021"),
    STAKE("스테이크","FD022","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD022"),
    IN_SAM("인삼","FD030","FD", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/FD030"),
    CHOCOLATE("초콜릿","SN000","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN000"),
    CANDY("사탕","SN001","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN001"),
    COLA("콜라","SN002","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN002"),
    COOKIE("쿠키","SN010","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN010"),
    CAKE("케이크","SN011","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN011"),
    FRY("감자튀김","SN012","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN012"),
    ICE_CREAM("아이스크림","SN013","SN", 0, 10D, 10D, 10D, 10D, 10D, "http://localhost:8002/resource/food/SN013")
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
    private final String resourceUrl;
}
