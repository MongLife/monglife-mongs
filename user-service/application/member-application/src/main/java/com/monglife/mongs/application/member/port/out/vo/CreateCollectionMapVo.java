package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMapVo {

    private final Long accountId;

    private final String mapCode;

    @Builder
    public CreateCollectionMapVo(Long accountId, String mapCode) {
        this.accountId = accountId;
        this.mapCode = mapCode;
    }
}
