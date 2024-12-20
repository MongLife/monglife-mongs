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
    USER_STORE_GET_PRODUCT(HttpStatus.OK.value(), "USER-STORE-000", "상품 조회에 성공했습니다."),
    USER_STORE_CREATE_ORDER(HttpStatus.OK.value(), "USER-STORE-001", "상품 주문 생성에 성공했습니다."),
    USER_STORE_CONSUME_ORDER(HttpStatus.OK.value(), "USER-STORE-002", "상품 주문 소비에 성공했습니다."),

    /**
     * 실패 응답
     */
    USER_STORE_GET_PRODUCTS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "USER-STORE-008", "상품 조회에 실패했습니다."),
    USER_STORE_CREATE_ORDER_FAIL(HttpStatus.BAD_REQUEST.value(), "USER-STORE-009", "주문 생성에 실패했습니다."),
    USER_STORE_INVALID_PURCHASE_VERIFY(HttpStatus.BAD_REQUEST.value(), "USER-STORE-010", "구매 검증에 실패했습니다."),
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
