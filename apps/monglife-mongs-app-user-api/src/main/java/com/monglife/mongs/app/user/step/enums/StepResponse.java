package com.monglife.mongs.app.user.step.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StepResponse implements Response {

    /**
     * 성공 응답
     */
    APP_USER_STEP_UPDATE_WALKING_COUNT(HttpStatus.OK.value(), "USER-STEP-000", "걸음수 동기화에 성공했습니다."),
    APP_USER_STEP_EXCHANGE_WALKING_COUNT(HttpStatus.OK.value(), "USER-STEP-001", "걸음수 감소에 성공했습니다."),
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
