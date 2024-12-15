package com.monglife.mongs.app.user.collection.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum CollectionResponse implements Response {

    /**
     * 성공 응답
     */
    USER_COLLECTION_CREATE_COLLECTION_MAP(HttpStatus.OK.value(), "USER-COLLECTION-000", "컬렉션 맵 등록에 성공했습니다."),
    USER_COLLECTION_CREATE_COLLECTION_MONG(HttpStatus.OK.value(), "USER-COLLECTION-001", "컬렉션 몽 등록에 성공했습니다."),
    USER_COLLECTION_GET_COLLECTION_MAP(HttpStatus.OK.value(), "USER-COLLECTION-002", "컬렉션 맵 조회에 성공했습니다."),
    USER_COLLECTION_GET_COLLECTION_MONG(HttpStatus.OK.value(), "USER-COLLECTION-003", "컬렉션 몽 조회에 성공했습니다."),
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
