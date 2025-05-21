package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMongVo {

    private final Long accountId;

    private final String mongTypeCode;

    @Builder
    public CreateCollectionMongVo(Long accountId, String mongTypeCode) {
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
    }
}
