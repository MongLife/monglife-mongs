package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongStepException extends ErrorException {

    public NotExistsMongStepException(String deviceId) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_MEMBER_STEP;
        this.result = Collections.singletonMap("deviceId", deviceId);
    }
}
