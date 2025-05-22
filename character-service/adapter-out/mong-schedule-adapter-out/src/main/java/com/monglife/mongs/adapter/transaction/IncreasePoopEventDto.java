package com.monglife.mongs.adapter.transaction;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class IncreasePoopEventDto {

    private Long mongId;

    @Builder
    public IncreasePoopEventDto(Long mongId) {
        this.mongId = mongId;
    }
}
