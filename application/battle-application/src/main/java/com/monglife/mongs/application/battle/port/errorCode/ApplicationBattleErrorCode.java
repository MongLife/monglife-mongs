package com.monglife.mongs.application.battle.port.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ApplicationBattleErrorCode implements ErrorCode {

    INVALID_CREATE_QUEUE_PLAYER("APPLICATION-BATTLE-000", "매치 대기열 등록에 실패했습니다."),
    INVALID_CREATE_MATCH("APPLICATION-BATTLE-001", "매치 등록에 실패했습니다."),
    NOT_EXISTS_QUEUE_PLAYER("APPLICATION-BATTLE-002", "매치 대기열이 존재하지 않습니다."),
    ;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto(Integer httpStatus) {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(Integer httpStatus, T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }
}
