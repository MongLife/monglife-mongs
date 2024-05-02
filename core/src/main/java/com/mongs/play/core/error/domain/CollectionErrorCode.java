package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectionErrorCode implements ErrorCode {
    NOT_FOUND_MAP_COLLECTION(HttpStatus.NOT_FOUND, "COLLECTION-101", "not found map collection."),
    NOT_FOUND_MONG_COLLECTION(HttpStatus.NOT_FOUND, "COLLECTION-101", "not found mong collection.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
