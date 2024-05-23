package com.mongs.play.module.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskCode {
    ZERO_EVOLUTION(60 * 5L),    // 5분 뒤에 부화
    DECREASE_STATUS(60 * 15L),  // 15분 뒤에 감소
    INCREASE_STATUS(60 * 15L),  // 15분 뒤에 증가
    INCREASE_POOP_COUNT(0L),    // 랜덤으로 생성
    DEAD_SATIETY(60 * 60 * 3L), // 3시간 이후 사망 (포만감 0 조건)
    DEAD_HEALTHY(60 * 60 * 3L), // 3시간 이후 사망 (체력 0 조건)
    ;

    private final Long expiration;
}
