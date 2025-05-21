package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateMongScheduleException extends ErrorException {

    public InvalidCreateMongScheduleException() {
        this.errorCode = ApplicationMongErrorCode.INVALID_CREATE_MONG_SCHEDULE;
        this.result = Collections.emptyMap();
    }
}
