package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMemberException extends ErrorException {

    public NotExistsMemberException(Long accountId) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_MEMBER;
        this.result = Collections.singletonMap("accountId", accountId);
    }
}
