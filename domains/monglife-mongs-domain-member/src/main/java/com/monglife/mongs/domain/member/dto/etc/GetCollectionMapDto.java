package com.monglife.mongs.domain.member.dto.etc;

import com.monglife.mongs.domain.member.entity.CollectionMapEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMapDto {

    private Long accountId;

    private String mapTypeCode;

    private String mapTypeName;

    @Builder
    public GetCollectionMapDto(Long accountId, String mapTypeCode, String mapTypeName) {
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
        this.mapTypeName = mapTypeName;
    }

    public static GetCollectionMapDto of(CollectionMapEntity collectionMapEntity) {
        return GetCollectionMapDto.builder()
                .accountId(collectionMapEntity.getAccountId())
                .mapTypeCode(collectionMapEntity.getComn().getCode())
                .mapTypeName(collectionMapEntity.getComn().getName())
                .build();
    }
}
