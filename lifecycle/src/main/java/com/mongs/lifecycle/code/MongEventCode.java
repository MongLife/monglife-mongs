package com.mongs.lifecycle.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongEventCode {
//    // 주기적 감소
//    WEIGHT_DOWN(60 * 15L, 5D),
//    STRENGTH_DOWN(60 * 15L, 5D),
//    SATIETY_DOWN(60 * 15L, 5D),
//    HEALTHY_DOWN(60 * 15L, 5D),
//    SLEEP_DOWN(60 * 15L, 5D),
//
//    // 주기적 증가
//    SLEEP_UP(60 * 15L, 0.5),
//    PAY_POINT_UP(60 * 15L, 5D),
//
//    // 랜덤 생성
//    POOP(60 * 60 * 4L, 1D),

//    // 죽음
//    DEAD(60 * 60 * 12L, 100D),

    // 주기적 감소
    WEIGHT_DOWN(15L, 5D),
    STRENGTH_DOWN(15L, 5D),
    SATIETY_DOWN(15L, 5D),
    HEALTHY_DOWN(15L, 5D),
    SLEEP_DOWN(15L, 5D),

    // 주기적 증가
    SLEEP_UP(15L, 0.5),
    PAY_POINT_UP(15L, 5D),

    // 랜덤 생성
    POOP(15L, 1D),

    // 죽음
    DEAD(5L, 100D),


    /* 관리자 코드 */
    ADMIN_DEAD(0L, 100D)
    ;

    private final Long expiration;
    private final Double value;
}
