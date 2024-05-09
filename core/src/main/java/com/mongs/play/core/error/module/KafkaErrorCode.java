package com.mongs.play.core.error.module;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KafkaErrorCode implements ErrorCode {
    TEST(HttpStatus.BAD_REQUEST, "KAFKA-000", "test error."),
    NOT_FOUND_TOPIC(HttpStatus.BAD_REQUEST, "KAFKA-100", "not found topic."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
