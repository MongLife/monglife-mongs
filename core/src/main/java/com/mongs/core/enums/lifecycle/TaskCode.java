package com.mongs.core.enums.lifecycle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskCode {

//    // 알
//    EGG(60 * 15L, null),                 // 15분 뒤에 부화
//    // 주기적 감소
//    WEIGHT_DOWN(60 * 15L, 0.5D),         // 15 분에 0.5 감소
//    STRENGTH_DOWN( 60 * 15L, 0.5D),      // 15 분에 0.5 감소
//    SATIETY_DOWN( 60 * 15L, 0.5D),       // 15 분에 0.5 감소
//    HEALTHY_DOWN( 60 * 15L, 0.5D),       // 15 분에 0.5 감소
//    SLEEP_DOWN( 60 * 15L, 0.5D),         // 15 분에 0.5 감소
//
//    // 주기적 증가
//    SLEEP_UP(60 * 15L, 0.5D),            // 15 분에 0.5 증가
//    PAY_POINT_UP(60 * 15L, 0.5D),        // 15 분에 0.5 증가
//
//    // 랜덤 생성
//    POOP(60 * 60 * 6L, 1D),              // 4 ~ 6 시간에 1개 씩 생성 (랜덤)
//
//    // 죽음 (조건)
//    DEAD_SATIETY(60 * 60 * 12L, null),   // 12 시간 이후 사망 (포만감 0 조건)
//    DEAD_HEALTHY(60 * 60 * 12L, null),   // 12 시간 이후 사망 (포만감 0 조건)
//    ;

    // 알
    EGG(15L, null),                     // 15초 뒤에 부화
    // 주기적 감소
    WEIGHT_DOWN(15L, 0.5D),             // 15 초에 0.5 감소
    STRENGTH_DOWN(15L, 0.5D),           // 15 초에 0.5 감소
    SATIETY_DOWN(15L, 0.5D),            // 15 초에 0.5 감소
    HEALTHY_DOWN(15L, 0.5D),            // 15 초에 0.5 감소
    SLEEP_DOWN( 15L, 0.5D),             // 15 초에 0.5 감소

    // 주기적 증가
    SLEEP_UP(15L, 0.5D),                // 15 초에 0.5 증가
    PAY_POINT_UP(15L, 0.5D),            // 15 초에 0.5 증가

    // 랜덤 생성
    POOP(60 * 60 * 6L, 1D),             // 4 ~ 6 시간에 1개 씩 생성 (랜덤)

    // 죽음 (조건)
    DEAD_SATIETY(60L, null),            // 1 시간 이후 사망 (포만감 0 조건)
    DEAD_HEALTHY(60L, null),            // 1 시간 이후 사망 (포만감 0 조건)
    ;



    private final Long expiration;
    private final Double value;
}
