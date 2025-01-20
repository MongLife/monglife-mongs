package com.monglife.mongs.client.manager.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ManagerResponse implements Response {

    /**
     * 실패 응답
     */
    CLIENT_MANAGER_CHARGE_PAY_POINT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CLIENT-MANAGER-100", "페이 포인트 증가에 실패했습니다."),
    CLIENT_MANAGER_PATCH_MONG_AFTER_TRAINING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CLIENT-MANAGER-101", "훈련 후 몽 정보 갱신에 실패했습니다."),
    CLIENT_MANAGER_GET_MINIMAL_MONG_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CLIENT-MANAGER-102", "몽 정보 조회에 실패했습니다."),
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
