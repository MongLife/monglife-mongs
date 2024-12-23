package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughWalkingCountException extends ErrorException {

    public NotEnoughWalkingCountException(Integer walkingCount) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_ENOUGH_WALKING_COUNT;
        this.result = Collections.singletonMap("walkingCount", walkingCount);
    }
}
