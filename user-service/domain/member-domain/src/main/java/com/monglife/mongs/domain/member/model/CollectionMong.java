package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMong {

    private final Long collectionMongId;

    private final Long accountId;

    private final String mongCode;

    private final String mongName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMong(Long collectionMongId, Long accountId, String mongCode, String mongName, Boolean isIncluded) {
        this.collectionMongId = collectionMongId;
        this.accountId = accountId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.isIncluded = isIncluded;
    }
}
