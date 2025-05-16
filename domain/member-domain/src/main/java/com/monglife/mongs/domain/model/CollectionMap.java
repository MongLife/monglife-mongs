package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMap {

    private final Long collectionMapId;

    private final Long accountId;

    private final String mapTypeCode;

    private final String mapTypeName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMap(Long collectionMapId, Long accountId, String mapTypeCode, String mapTypeName, Boolean isIncluded) {
        this.collectionMapId = collectionMapId;
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
        this.mapTypeName = mapTypeName;
        this.isIncluded = isIncluded;
    }
}
