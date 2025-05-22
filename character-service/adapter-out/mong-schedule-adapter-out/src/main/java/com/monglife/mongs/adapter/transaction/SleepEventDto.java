package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SleepEventDto {

    private Long mongId;

    @Builder
    public SleepEventDto(Long mongId) {
        this.mongId = mongId;
    }
}
