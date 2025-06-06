package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SleepEventDto {

    private Long accountId;

    private Long mongId;

    @Builder
    public SleepEventDto(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
