package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
