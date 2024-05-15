package com.mongs.play.module.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskCode {
    // 알
    ZERO_EVOLUTION(5L),                     // 15초 뒤에 부화
    // 주기적 감소
    DECREASE_STATUS(15L),             // 30 초에 0.5 감소
    // 주기적 증가
    INCREASE_STATUS(15L),                 // 15 초에 0.5 증가
    // 랜덤 생성
    INCREASE_POOP_COUNT(15L),             // 4 ~ 6 시간에 1개 씩 생성 (랜덤)

    // 죽음 (조건)
    DEAD_SATIETY(15L),            // 1 분 이후 사망 (포만감 0 조건)
    DEAD_HEALTHY(15L),            // 1 분 이후 사망 (체력 0 조건)
    ;

    private final Long expiration;
}
