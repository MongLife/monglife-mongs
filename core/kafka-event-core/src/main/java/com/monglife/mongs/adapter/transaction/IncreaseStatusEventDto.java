package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class IncreaseStatusEventDto {

    private Long accountId;

    private Long mongId;

    @Builder
    public IncreaseStatusEventDto(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
