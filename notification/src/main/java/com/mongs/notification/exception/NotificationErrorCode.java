package com.mongs.notification.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    NOTIFICATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-100", "Notification Fail"),
    CONNECT_REFUSE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-101", "Mqtt Connect Fail"),
    GENERATE_DATA_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-102", "Generate Data Fail (Json Paring Fail)"),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
