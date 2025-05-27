package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughRandomDrawTicketException extends ErrorException {

    public NotEnoughRandomDrawTicketException() {
        this.errorCode = DomainMongErrorCode.NOT_ENOUGH_RANDOM_DRAW_TICKET;
        this.result = Collections.emptyMap();
    }
}
