package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CreateMongEventDto {

    private Long accountId;

    private String mongCode;

    @Builder
    public CreateMongEventDto(Long accountId, String mongCode) {
        this.accountId = accountId;
        this.mongCode = mongCode;
    }
}
