package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMong {

    private final Long collectionMongId;

    private final Long accountId;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMong(Long collectionMongId, Long accountId, String mongTypeCode, String mongTypeName, Boolean isIncluded) {
        this.collectionMongId = collectionMongId;
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.isIncluded = isIncluded;
    }
}
