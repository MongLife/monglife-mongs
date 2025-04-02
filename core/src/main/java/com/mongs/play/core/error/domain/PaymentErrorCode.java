package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
    INVALID_RECEIPT(HttpStatus.BAD_REQUEST, "PAYMENT-100", "invalid receipt information."),
    NOT_FOUND_PAYMENT_LOG(HttpStatus.NOT_FOUND, "PAYMENT-101", "not found payment log."),
    NOT_FOUND_CHARGE_ITEM(HttpStatus.NOT_FOUND, "PAYMENT-102", "not found charge item."),
    NOT_FOUND_EXCHANGE_ITEM(HttpStatus.NOT_FOUND, "PAYMENT-103", "not found exchange item."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
