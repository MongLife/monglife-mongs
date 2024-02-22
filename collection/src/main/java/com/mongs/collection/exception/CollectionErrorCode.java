package com.mongs.collection.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectionErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COLLECTION-100", "Invalid Parameter"),
    MAP_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COLLECTION-101", "Invalid Map Code"),
    MONG_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COLLECTION-102", "Invalid Mong Code");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
