package com.monglife.mongs.app.management.global.code.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedCode {

    FD000(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "별사탕"),
    FD010(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "사과"),
    FD011(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "삼각김밥"),
    FD012(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "샌드위치"),
    FD020(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "피자"),
    FD021(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "치킨"),
    FD022(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "스테이크"),
    FD030(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "인삼"),
    SN000(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "초콜릿"),
    SN001(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "사탕"),
    SN002(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "콜라"),
    SN010(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "쿠키"),
    SN011(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "케이크"),
    SN012(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "감자튀김"),
    SN013(10D, 10D, 10D, 10D, 10D, 60 * 15, 0, "아이스크림")
    ;

    public final Double incWeightValue;
    public final Double incStrengthValue;
    public final Double incSatietyValue;
    public final Double incHealthyValue;
    public final Double incSleepValue;
    public final Integer delaySeconds;
    public final Integer price;
    public final String name;
}
