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

    @Builder
    public CollectionMong(Long collectionMongId, Long accountId, String mongTypeCode) {
        this.collectionMongId = collectionMongId;
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
    }
}
