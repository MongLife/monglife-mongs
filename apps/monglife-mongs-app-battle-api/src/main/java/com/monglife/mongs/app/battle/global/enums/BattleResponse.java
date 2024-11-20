package com.monglife.mongs.app.battle.global.enums;

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
     * 성공 응답
     */
    BATTLE_CREATE_WAIT_MATCHING(HttpStatus.OK.value(), "BATTLE-000", "배틀 매칭 대기열에 등록되었습니다."),
    BATTLE_DELETE_WAIT_MATCHING(HttpStatus.OK.value(), "BATTLE-001", "배틀 매칭 대기열에서 삭제되었습니다."),
    BATTLE_FIND_MATCHING(HttpStatus.OK.value(), "BATTLE-002", "배틀 매칭에 성공했습니다."),
    BATTLE_ENTER_ALL_BATTLE_PLAYER(HttpStatus.OK.value(), "BATTLE-002", "모든 플레이어가 입장했습니다."),
    BATTLE_OVER_BATTLE(HttpStatus.OK.value(), "BATTLE-002", "배틀이 종료되었습니다."),
    BATTLE_FIGHT_BATTLE(HttpStatus.OK.value(), "BATTLE-002", "라운드가 종료되었습니다."),

    /**
     * 실패 응답
     */
    BATTLE_NOT_EXISTS_BATTLE_RESPONSE(HttpStatus.NOT_ACCEPTABLE.value(), "BATTLE-100", "유효한 응답이 없습니다."),
    BATTLE_NOT_EXISTS_WAIT_MATCHING(HttpStatus.BAD_REQUEST.value(), "BATTLE-100", "매칭 대기열이 존재하지 않습니다."),
    BATTLE_NOT_EXISTS_ROOM_ID(HttpStatus.BAD_REQUEST.value(), "BATTLE-100", "존재하지 않는 배틀 룸 ID 입니다."),
    BATTLE_NOT_EXISTS_PLAYER_ID(HttpStatus.BAD_REQUEST.value(), "BATTLE-101", "존재하지 않는 배틀 플레이어 ID 입니다."),
    BATTLE_ONLY_BOT_MATCHING(HttpStatus.NOT_ACCEPTABLE.value(), "BATTLE-100", "봇으로만 매칭이 이루어졌습니다."),
    BATTLE_ALREADY_EXISTS_ROUND(HttpStatus.BAD_REQUEST.value(), "BATTLE-101", "이미 라운드 선택이 끝났습니다."),

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
