package com.monglife.mongs.adapter.in.battle.web.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterInBattleWebResponse implements Response {

    CREATE_QUEUE_PLAYER(HttpStatus.OK.value(), "100-100-000", "매치 대기열 등록에 성공했습니다."),
    DELETE_QUEUE_PLAYER(HttpStatus.OK.value(), "100-100-001", "매치 대기열 삭제에 성공했습니다."),
    GET_MATCH_OUT_COME(HttpStatus.OK.value(), "100-100-002", "매치 보상 정보 조회에 성공했습니다."),
    GET_MATCH(HttpStatus.OK.value(), "100-100-003", "매치 조회에 성공했습니다."),
    GET_WIN_MATCH_PLAYER(HttpStatus.OK.value(), "100-100-004", "승리 매치 플레이어 조회에 성공했습니다."),
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
