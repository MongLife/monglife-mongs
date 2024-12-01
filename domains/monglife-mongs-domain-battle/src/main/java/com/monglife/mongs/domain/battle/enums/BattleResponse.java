package com.monglife.mongs.domain.battle.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum BattleResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_BATTLE_NOT_EXISTS_WAIT_MATCHING(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-102", "매칭 대기열이 존재하지 않습니다."),
    DOMAIN_BATTLE_NOT_EXISTS_ROOM_ID(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-103", "존재하지 않는 배틀 룸 ID 입니다."),
    DOMAIN_BATTLE_NOT_EXISTS_PLAYER_ID(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-104", "존재하지 않는 배틀 플레이어 ID 입니다."),
    DOMAIN_BATTLE_NOT_EXISTS_MONG_ID(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-105", "존재하지 않는 몽 ID 입니다."),
    DOMAIN_BATTLE_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-106", "몽 타입이 존재하지 않습니다."),
    DOMAIN_BATTLE_ONLY_BOT_MATCHING(HttpStatus.NOT_ACCEPTABLE.value(), "DOMAIN-BATTLE-107", "봇으로만 매칭이 이루어졌습니다."),
    DOMAIN_BATTLE_ALREADY_EXISTS_ROUND(HttpStatus.BAD_REQUEST.value(), "DOMAIN-BATTLE-108", "이미 라운드 선택이 끝났습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto() {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }

}
