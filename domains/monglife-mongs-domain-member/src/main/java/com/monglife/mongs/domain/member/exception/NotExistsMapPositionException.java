package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMapPositionException extends ErrorException {

    public NotExistsMapPositionException(Long mapPositionId) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_MAP_POSITION;
        this.result = Collections.singletonMap("mapPositionId", mapPositionId);
    }
}
