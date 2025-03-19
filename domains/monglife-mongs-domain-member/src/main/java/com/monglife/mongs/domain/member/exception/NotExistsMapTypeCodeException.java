package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMapTypeCodeException extends ErrorException {

    public NotExistsMapTypeCodeException(String mapTypeCode) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_MAP_TYPE;
        this.result = Collections.singletonMap("mapTypeCode", mapTypeCode);
    }
}
