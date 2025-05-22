package com.monglife.mongs.adapter.transaction;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class IncreaseStatusEventDto {

    private Long mongId;

    @Builder
    public IncreaseStatusEventDto(Long mongId) {
        this.mongId = mongId;
    }
}
