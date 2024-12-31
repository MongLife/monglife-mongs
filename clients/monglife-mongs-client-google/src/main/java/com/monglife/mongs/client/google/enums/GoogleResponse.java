package com.monglife.mongs.client.google.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum GoogleResponse implements Response {

    /**
     * 실패 응답
     */
    CLIENT_GOOGLE_GET_PRODUCTS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GOOGLE-100", "상품 조회에 실패했습니다."),
    CLIENT_GOOGLE_GET_ORDER_FAIL(HttpStatus.BAD_REQUEST.value(), "GOOGLE-101", "주문 조회에 실패했습니다."),
    CLIENT_GOOGLE_CONSUME_ORDER(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GOOGLE-102", "주문 소비에 실패했습니다."),
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
