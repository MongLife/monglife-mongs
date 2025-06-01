package com.monglife.mongs.adapter.out.battle.publish.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishBattleResponse implements Response {

    BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER(HttpStatus.OK.value(), "200-100-000", "배틀 매칭에 성공했습니다."),
    BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER_FAIL(HttpStatus.NOT_ACCEPTABLE.value(), "200-100-001", "배틀 매칭에 실패했습니다."),
    BATTLE_PUBLISH_MATCH_PLAYERS_ENTERED(HttpStatus.OK.value(), "200-100-002", "모든 플레이어가 입장했습니다."),
    BATTLE_PUBLISH_MATCH(HttpStatus.OK.value(), "200-100-003", "라운드가 종료되었습니다."),
    BATTLE_PUBLISH_MATCH_END(HttpStatus.OK.value(), "200-100-004", "배틀이 종료되었습니다."),
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
