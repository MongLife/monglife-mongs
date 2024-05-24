package com.mongs.play.module.task.enums;

public enum TaskCode {
    ZERO_EVOLUTION,         // 5분 뒤에 부화
    DECREASE_STATUS,        // 15분 뒤에 감소
    INCREASE_STATUS,        // 15분 뒤에 증가
    INCREASE_POOP_COUNT,    // 랜덤으로 생성
    DEAD_SATIETY,           // 3시간 이후 사망 (포만감 0 조건)
    DEAD_HEALTHY,           // 3시간 이후 사망 (체력 0 조건)
    ;
}
