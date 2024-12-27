package com.monglife.mongs.client.user.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum UserResponse implements Response {

    /**
     * 실패 응답
     */
    CLIENT_USER_CREATE_COLLECTION_MAP(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CLIENT-USER-100", "맵 컬렉션 등록에 실패했습니다."),
    CLIENT_USER_CREATE_COLLECTION_MONG(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CLIENT-USER-101", "몽 컬렉션 등록에 실패했습니다."),
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
