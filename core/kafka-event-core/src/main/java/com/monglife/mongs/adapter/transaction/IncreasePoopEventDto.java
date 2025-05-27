package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncreasePoopEventDto {

    private Long accountId;

    private Long mongId;

    @Builder
    public IncreasePoopEventDto(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
