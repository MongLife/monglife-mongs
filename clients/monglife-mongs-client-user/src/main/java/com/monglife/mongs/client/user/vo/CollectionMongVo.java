package com.monglife.mongs.client.user.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CollectionMongVo {

    private final String mongTypeCode;

    private final String mongTypeName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMongVo(String mongTypeCode, String mongTypeName, Boolean isIncluded) {
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.isIncluded = isIncluded;
    }
}
