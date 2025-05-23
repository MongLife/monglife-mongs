package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMongEventDto {

    private Long accountId;

    private String mongTypeCode;

    @Builder
    public CreateMongEventDto(Long accountId, String mongTypeCode) {
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
    }
}
