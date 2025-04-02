package com.monglife.mongs.domain.match.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MatchResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_MATCH_NOT_EXISTS_ROOM_ID(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MATCH-100", "존재하지 않는 배틀 룸 ID 입니다."),
    DOMAIN_MATCH_NOT_EXISTS_PLAYER_ID(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MATCH-101", "존재하지 않는 배틀 플레이어 ID 입니다."),
    DOMAIN_MATCH_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MATCH-102", "몽 타입이 존재하지 않습니다."),
    DOMAIN_MATCH_ONLY_BOT_MATCHING(HttpStatus.NOT_ACCEPTABLE.value(), "DOMAIN-MATCH-103", "봇으로만 매칭이 이루어졌습니다."),
    DOMAIN_MATCH_NOT_EXISTS_PLAYER(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DOMAIN-MATCH-104", "플레이어가 존재하지 않습니다."),
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
