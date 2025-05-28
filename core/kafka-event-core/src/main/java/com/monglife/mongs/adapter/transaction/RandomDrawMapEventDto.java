package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class RandomDrawMapEventDto {

    private Long accountId;

    private String mapCode;

    @Builder
    public RandomDrawMapEventDto(Long accountId, String mapCode) {
        this.accountId = accountId;
        this.mapCode = mapCode;
    }
}
