package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMapVo {

    private final Long accountId;

    private final String mapTypeCode;

    @Builder
    public CreateCollectionMapVo(Long accountId, String mapTypeCode) {
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
    }
}
