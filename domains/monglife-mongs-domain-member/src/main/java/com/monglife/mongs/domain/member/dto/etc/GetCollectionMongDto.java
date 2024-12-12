package com.monglife.mongs.domain.member.dto.etc;

import com.monglife.mongs.domain.member.entity.CollectionMongEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMongDto {

    private Long accountId;

    private String mongTypeCode;

    private String mongTypeName;

    @Builder
    public GetCollectionMongDto(Long accountId, String mongTypeCode, String mongTypeName) {
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
    }

    public static GetCollectionMongDto of(CollectionMongEntity collectionMongEntity) {
        return GetCollectionMongDto.builder()
                .accountId(collectionMongEntity.getAccountId())
                .mongTypeCode(collectionMongEntity.getComn().getCode())
                .mongTypeName(collectionMongEntity.getComn().getName())
                .build();
    }
}
