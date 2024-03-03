package com.mongs.common.code;

public enum TestFoodCode {
    STAR_CANDY("별사탕","FD000", "FD", 0, 0),
    APPLE("사과", "FD010","FD", 0, 0),
    KIM_BAB("삼각김밥", "FD011","FD", 0, 0),
    SANDWICH("샌드위치" ,"FD012","FD", 0, 0),
    PIZZA("피자","FD020","FD", 0, 0),
    CHICKEN("치킨","FD021","FD", 0, 0),
    STAKE("스테이크","FD022","FD", 0, 0),
    IN_SAM("인삼","FD030","FD", 0, 0),
    CHOCOLATE("초콜릿","SN000","SN", 0, 0),
    CANDY("사탕","SN001","SN", 0, 0),
    COLA("콜라","SN002","SN", 0, 0),
    COOKIE("쿠키","SN010","SN", 0, 0),
    CAKE("케이크","SN011","SN", 0, 0),
    FRY("감자튀김","SN012","SN", 0, 0),
    ICE_CREAM("아이스크림","SN013","SN", 0, 0)
    ;

    private final String code;
    private final String name;
    private final String groupCode;
    private final Integer fullness;
    private final Integer point;

    TestFoodCode(String code, String name, String groupCode, Integer fullness, Integer point) {
        this.code = code;
        this.name = name;
        this.groupCode = groupCode;
        this.fullness = fullness;
        this.point = point;
    }

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public String getGroupCode() {
        return this.groupCode;
    }
    public Integer getFullness() {
        return this.fullness;
    }
    public Integer getPoint() {
        return this.point;
    }
}
