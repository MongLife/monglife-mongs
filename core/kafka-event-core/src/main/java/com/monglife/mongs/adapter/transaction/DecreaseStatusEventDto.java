package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
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
