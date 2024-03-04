package com.mongs.management.domain.food;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitFoodCodeData {
    STAR_CANDY("별사탕","FD000", "FD", 0, 0, "STAR_CANDY"),
    APPLE("사과", "FD010","FD", 0, 0, "APPLE"),
    KIM_BAB("삼각김밥", "FD011","FD", 0, 0, "KIM_BAB"),
    SANDWICH("샌드위치" ,"FD012","FD", 0, 0, "SANDWICH"),
    PIZZA("피자","FD020","FD", 0, 0, "PIZZA"),
    CHICKEN("치킨","FD021","FD", 0, 0, "CHICKEN"),
    STAKE("스테이크","FD022","FD", 0, 0, "STAKE"),
    IN_SAM("인삼","FD030","FD", 0, 0, "IN_SAM"),
    CHOCOLATE("초콜릿","SN000","SN", 0, 0, "CHOCOLATE"),
    CANDY("사탕","SN001","SN", 0, 0, "CANDY"),
    COLA("콜라","SN002","SN", 0, 0, "COLA"),
    COOKIE("쿠키","SN010","SN", 0, 0, "COOKIE"),
    CAKE("케이크","SN011","SN", 0, 0, "CAKE"),
    FRY("감자튀김","SN012","SN", 0, 0, "FRY"),
    ICE_CREAM("아이스크림","SN013","SN", 0, 0, "ICE_CREAM")
    ;

    private final String code;
    private final String name;
    private final String groupCode;
    private final Integer fullness;
    private final Integer point;
    private final String  value;
}
