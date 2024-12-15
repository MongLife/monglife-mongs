package com.monglife.mongs.domain.member.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MemberResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_MEMBER_NOT_EXISTS_MEMBER(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MEMBER-100", "존재하지 않는 회원입니다."),
    DOMAIN_MEMBER_NOT_EXISTS_MAP_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-101", "맵 타입이 존재하지 않습니다."),
    DOMAIN_MEMBER_NOT_EXISTS_MAP_POSITION(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-102", "맵 위치 정보가 존재하지 않습니다."),
    DOMAIN_MEMBER_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-103", "몽 타입이 존재하지 않습니다."),
    DOMAIN_MEMBER_NOT_EXISTS_STAR_POINT(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-104", "충분한 스타 포인트가 없습니다."),
    DOMAIN_MEMBER_NOT_EXISTS_WALKING_COUNT(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-105", "충분한 걸음 수가 없습니다."),
    DOMAIN_MEMBER_NOT_EXISTS_PAYMENT_CODE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-106", "결제 코드가 존재하지 않습니다."),
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
