package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RandomDrawMapEventDto {

    private Long accountId;

    private String mapTypeCode;

    @Builder
    public RandomDrawMapEventDto(Long accountId, String mapTypeCode) {
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
    }
}
