package com.monglife.mongs.domain.member.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CollectionMapVo {

    private final String mapTypeCode;

    private final String mapTypeName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMapVo(String mapTypeCode, String mapTypeName, Boolean isIncluded) {
        this.mapTypeCode = mapTypeCode;
        this.mapTypeName = mapTypeName;
        this.isIncluded = isIncluded;
    }
}
