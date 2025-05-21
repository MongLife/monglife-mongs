package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsTrainingTypeException extends ErrorException {

    public NotExistsTrainingTypeException() {
        this.errorCode = ApplicationMongErrorCode.NOT_EXISTS_TRAINING_TYPE;
        this.result = Collections.emptyMap();
    }
}
