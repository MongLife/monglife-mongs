package com.monglife.mongs.domain.task.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TaskResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_TASK_NOT_EXISTS_TASK_CODE(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-100", "테스크 코드가 존재하지 않습니다."),
    DOMAIN_TASK_NOT_EXISTS_TASK(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-100", "테스크가 존재하지 않습니다."),
    DOMAIN_TASK_ALREADY_EXISTS_TASK(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-100", "이미 테스크가 존재합니다."),
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
