package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeadEventDto {

    private Long mongId;

    @Builder
    public DeadEventDto(Long mongId) {
        this.mongId = mongId;
    }
}
