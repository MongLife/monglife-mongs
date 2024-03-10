package com.mongs.lifecycle.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskCode {
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
    WEIGHT_DOWN(5L, 10D),
    STRENGTH_DOWN(5L, 10D),
    SATIETY_DOWN(5L, 10D),
    HEALTHY_DOWN(5L, 10D),
    SLEEP_DOWN(5L, 10D),

    // 주기적 증가
    SLEEP_UP(5L, 5D),
    PAY_POINT_UP(5L, 5D),

    // 랜덤 생성
    POOP(5L, 1D),

    // 죽음 (조건)
    DEAD_SATIETY(10L, -1D),
    DEAD_HEALTHY(20L, -1D),
    DEAD(0L, -1D),
    ;

    private final Long expiration;
    private final Double value;
}
