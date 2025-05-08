package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMap {

    private Long collectionMapId;

    private Long accountId;

    private String mapTypeCode;

    private String mapTypeName;

    private Boolean isIncluded;

    @Builder
    public CollectionMap(Long collectionMapId, Long accountId, String mapTypeCode, String mapTypeName, Boolean isIncluded) {
        this.collectionMapId = collectionMapId;
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
        this.mapTypeName = mapTypeName;
        this.isIncluded = isIncluded;
    }
}
