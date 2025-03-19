package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsStarPointException extends ErrorException {

    public NotExistsStarPointException(Integer starPoint) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_STAR_POINT;
        this.result = Collections.singletonMap("starPoint", starPoint);
    }
}
