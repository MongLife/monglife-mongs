package com.monglife.mongs.adapter.in.member.event.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMongEventDto {

    private Long accountId;

    private String mongTypeCode;

    @Builder
    public CreateCollectionMongEventDto(Long accountId, String mongTypeCode) {
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
    }
}
