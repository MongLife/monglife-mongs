package com.monglife.mongs.app.activity.battle.enums;

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
    // ACTIVITY - BATTLE
    ACTIVITY_BATTLE_CREATE_WAIT_MATCHING(HttpStatus.OK.value(), "ACTIVITY-BATTLE-000", "배틀 매칭 대기열에 등록되었습니다."),
    ACTIVITY_BATTLE_DELETE_WAIT_MATCHING(HttpStatus.OK.value(), "ACTIVITY-BATTLE-001", "배틀 매칭 대기열에서 삭제되었습니다."),
    ACTIVITY_BATTLE_FIND_MATCHING(HttpStatus.OK.value(), "ACTIVITY-BATTLE-002", "배틀 매칭에 성공했습니다."),
    ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER(HttpStatus.OK.value(), "ACTIVITY-BATTLE-003", "모든 플레이어가 입장했습니다."),
    ACTIVITY_BATTLE_OVER_BATTLE(HttpStatus.OK.value(), "ACTIVITY-BATTLE-004", "배틀이 종료되었습니다."),
    ACTIVITY_BATTLE_FIGHT_BATTLE(HttpStatus.OK.value(), "ACTIVITY-BATTLE-005", "라운드가 종료되었습니다."),

    /**
     * 실패 응답
     */
    ACTIVITY_BATTLE_NOT_EXISTS_OVER_MATCH(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-100", "완료된 매치를 찾을 수 없습니다."),
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
