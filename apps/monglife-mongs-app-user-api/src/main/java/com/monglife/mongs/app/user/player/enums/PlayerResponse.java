package com.monglife.mongs.app.user.player.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PlayerResponse implements Response {

    /**
     * 성공 응답
     */
    APP_USER_PLAYER_OBSERVE_MEMBER(HttpStatus.OK.value(), "USER-PLAYER-000", "회원 정보에 변동이 있습니다."),
    APP_USER_PLAYER_CREATE_PLAYER(HttpStatus.OK.value(), "USER-PLAYER-001", "플레이어 등록에 성공했습니다."),
    APP_USER_PLAYER_GET_PLAYER(HttpStatus.OK.value(), "USER-PLAYER-002", "플레이어 조회에 성공했습니다."),
    APP_USER_PLAYER_INCREASE_SLOT(HttpStatus.OK.value(), "USER-PLAYER-003", "슬롯 수 증가에 성공했습니다."),
    APP_USER_PLAYER_SYNC_WALKING_COUNT(HttpStatus.OK.value(), "USER-PLAYER-004", "걸음수 동기화에 성공했습니다."),
    APP_USER_PLAYER_EXCHANGE_WALKING_COUNT(HttpStatus.OK.value(), "USER-PLAYER-005", "걸음수 감소에 성공했습니다."),
    APP_USER_PLAYER_EXCHANGE_STAR_POINT(HttpStatus.OK.value(), "USER-PLAYER-006", "스타포인트 환전에 성공했습니다."),

    /**
     * 실패 응답
     */
    APP_USER_PLAYER_ALREADY_MAX_SLOT_COUNT(HttpStatus.BAD_REQUEST.value(), "USER-PLAYER-100", "더이상 슬롯을 확장할 수 없습니다."),
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
