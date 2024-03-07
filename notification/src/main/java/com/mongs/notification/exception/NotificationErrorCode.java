package com.mongs.notification.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    CONNECT_REFUSE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-100", "MQTT 로의 연결을 할 수 없습니다."),
    NOTIFICATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-101", "전송에 실패했습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
