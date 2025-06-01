package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectionMap {

    private final Long collectionMapId;

    private final Long accountId;

    private final String mapCode;

    private final String mapName;

    private final Boolean isIncluded;

    @Builder
    public CollectionMap(Long collectionMapId, Long accountId, String mapCode, String mapName, Boolean isIncluded) {
        this.collectionMapId = collectionMapId;
        this.accountId = accountId;
        this.mapCode = mapCode;
        this.mapName = mapName;
        this.isIncluded = isIncluded;
    }
}
