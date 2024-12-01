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

    // ACTIVITY - TRAINING
    ACTIVITY_TRAINING_END_RUNNER(HttpStatus.OK.value(), "ACTIVITY-TRAINING-000", "훈련 RUNNER 완료했습니다."),


    /**
     * 실패 응답
     */
    // ACTIVITY - BATTLE
    ACTIVITY_BATTLE_NOT_EXISTS_BATTLE_ROUND_CODE_RESPONSE(HttpStatus.NOT_ACCEPTABLE.value(), "ACTIVITY-BATTLE-101", "존재하지 않는 배틀 요청 코드입니다."),
    ACTIVITY_BATTLE_NOT_EXISTS_WAIT_MATCHING(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-102", "매칭 대기열이 존재하지 않습니다."),
    ACTIVITY_BATTLE_NOT_EXISTS_ROOM_ID(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-103", "존재하지 않는 배틀 룸 ID 입니다."),
    ACTIVITY_BATTLE_NOT_EXISTS_PLAYER_ID(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-104", "존재하지 않는 배틀 플레이어 ID 입니다."),
    ACTIVITY_BATTLE_NOT_EXISTS_MONG_ID(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-105", "존재하지 않는 몽 ID 입니다."),
    ACTIVITY_BATTLE_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-106", "몽 타입이 존재하지 않습니다."),
    ACTIVITY_BATTLE_ONLY_BOT_MATCHING(HttpStatus.NOT_ACCEPTABLE.value(), "ACTIVITY-BATTLE-107", "봇으로만 매칭이 이루어졌습니다."),
    ACTIVITY_BATTLE_ALREADY_EXISTS_ROUND(HttpStatus.BAD_REQUEST.value(), "ACTIVITY-BATTLE-108", "이미 라운드 선택이 끝났습니다."),

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
