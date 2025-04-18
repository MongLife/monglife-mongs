package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMong {

    private Long collectionMongId;

    private Long accountId;

    private String mongTypeCode;

    private Boolean isIncluded;

    @Builder
    public CollectionMong(Long collectionMongId, Long accountId, String mongTypeCode, Boolean isIncluded) {
        this.collectionMongId = collectionMongId;
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
        this.isIncluded = isIncluded;
    }
}
