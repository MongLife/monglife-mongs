package com.mongs.collection.exception;

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
    NOT_FOUND_MAP_CODE(HttpStatus.BAD_REQUEST, "COLLECTION-103", "Not Found Map Code"),
    NOT_FOUND_MONG_CODE(HttpStatus.BAD_REQUEST, "COLLECTION-104", "Not Found Mong Code");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
