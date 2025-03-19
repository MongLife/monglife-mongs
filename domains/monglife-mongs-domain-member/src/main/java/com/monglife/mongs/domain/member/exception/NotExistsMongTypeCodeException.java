package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException(String mongTypeCode) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.singletonMap("mongTypeCode", mongTypeCode);
    }
}
