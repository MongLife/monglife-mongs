package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsWalkingCountException extends ErrorException {

    public NotExistsWalkingCountException(Integer walkingCount) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_WALKING_COUNT;
        this.result = Collections.singletonMap("walkingCount", walkingCount);
    }
}
