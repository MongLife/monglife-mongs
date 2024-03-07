package com.mongs.lifecycle.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongEventCode {
    // 주기적 감소
    WEIGHT_DOWN(1000 * 60 * 15, 5),
    STRENGTH_DOWN(1000 * 60 * 15, 5),
    SATIETY_DOWN(1000 * 60 * 15, 5),
    HEALTHY_DOWN(1000 * 60 * 15, 5),
    SLEEP_DOWN(1000 * 60 * 15, 5),

    // 주기적 증가
    SLEEP_UP(1000 * 60 * 15, 5),
    PAY_POINT_UP(1000 * 60 * 15, 5),

    // 랜덤 생성
    POOP(-1, 1),
    ;

    private final Integer expiration;
    private final Integer value;
}
