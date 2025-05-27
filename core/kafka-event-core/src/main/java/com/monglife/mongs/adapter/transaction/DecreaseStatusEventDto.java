package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DecreaseStatusEventDto {

    private Long accountId;

    private Long mongId;

    @Builder
    public DecreaseStatusEventDto(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
