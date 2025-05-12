package com.monglife.mongs.domain.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DomainBattleErrorCode implements ErrorCode {
    
    ;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto(Integer httpStatus) {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(Integer httpStatus, T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }
}
