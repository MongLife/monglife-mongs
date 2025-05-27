package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMongVo {

    private final Long accountId;

    private final String mongCode;

    @Builder
    public CreateCollectionMongVo(Long accountId, String mongCode) {
        this.accountId = accountId;
        this.mongCode = mongCode;
    }
}
