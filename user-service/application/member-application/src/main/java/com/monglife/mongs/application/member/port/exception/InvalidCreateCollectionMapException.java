package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateCollectionMapException extends ErrorException {

    public InvalidCreateCollectionMapException() {
        this.errorCode = ApplicationMemberErrorCode.INVALID_CREATE_COLLECTION_MAP;
        this.result = Collections.emptyMap();
    }
}
