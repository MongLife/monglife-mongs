package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class EvolutionMongEventDto {

    private Long accountId;

    private String mongCode;

    @Builder
    public EvolutionMongEventDto(Long accountId, String mongCode) {
        this.accountId = accountId;
        this.mongCode = mongCode;
    }
}
