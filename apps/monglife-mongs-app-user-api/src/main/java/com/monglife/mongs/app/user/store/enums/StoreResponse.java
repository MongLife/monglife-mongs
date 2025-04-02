package com.monglife.mongs.app.user.store.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StoreResponse implements Response {

    /**
     * 성공 응답
     */
    APP_USER_STORE_GET_PRODUCT(HttpStatus.OK.value(), "USER-STORE-000", "상품 조회에 성공했습니다."),
    APP_USER_STORE_CONSUME_ORDER(HttpStatus.OK.value(), "USER-STORE-001", "상품 주문 소비에 성공했습니다."),
    APP_USER_STORE_GET_PRODUCT_ORDER(HttpStatus.OK.value(), "USER-STORE-002", "상품 주문 조회에 성공했습니다."),

    APP_USER_STORE_NOT_PURCHASE_ORDER(HttpStatus.BAD_REQUEST.value(), "USER-STORE-100", "구매 처리가 완료되지 않았습니다."),
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
