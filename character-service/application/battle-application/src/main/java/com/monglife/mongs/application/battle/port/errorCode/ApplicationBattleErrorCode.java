package com.monglife.mongs.application.battle.port.errorCode;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationBattleErrorCode implements ErrorCode {

    INVALID_CREATE_QUEUE_PLAYER("400-100-000", "매치 대기열 등록에 실패했습니다."),
    INVALID_CREATE_MATCH("400-100-001", "매치 등록에 실패했습니다."),
    NOT_EXISTS_QUEUE_PLAYER("400-100-002", "매치 대기열이 존재하지 않습니다."),
    NOT_EXISTS_MATCH("400-100-003", "매치가 존재하지 않습니다."),
    NOT_EXISTS_MONG("400-100-004", "몽이 존재하지 않습니다."),
    NOT_END_MATCH("400-100-005", "아직 종료되지 않은 매치 입니다."),
    ;

    private final String code;

    private final String message;
}
