package com.monglife.mongs.domain.battle.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DomainBattleErrorCode implements ErrorCode {

    ALREADY_ENTER_MATCH_PLAYER("DOMAIN-BATTLE-000", "이미 입장한 매치 플레이어 입니다."),
    ALREADY_EXIT_MATCH_PLAYER("DOMAIN-BATTLE-001", "이미 퇴장한 매치 플레이어 입니다."),
    ALREADY_START_MATCH("DOMAIN-BATTLE-002", "이미 시작한 매치 입니다."),
    ALREADY_EXISTS_MATCH_PICK("DOMAIN-BATTLE-003", "이미 현재 라운드에 선택을 완료했습니다."),
    NOT_EXISTS_MATCH_PLAYER("DOMAIN-BATTLE-004", "매치 플레이어가 존재하지 않습니다."),
    NOT_PICKED_ALL_MATCH_PLAYERS("DOMAIN-BATTLE-005", "모든 매치 플레이어가 매치 선택을 하지 않았습니다"),
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
