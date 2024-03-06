package com.mongs.member.domain.collection.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectionErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COLLECTION-100", "Invalid Parameter"),
    INVALID_MAP_CODE(HttpStatus.BAD_REQUEST, "COLLECTION-101", "Invalid Map Code"),
    INVALID_MONG_CODE(HttpStatus.BAD_REQUEST, "COLLECTION-102", "Invalid Map Code"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COLLECTION-103", "Internal Server Error")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
