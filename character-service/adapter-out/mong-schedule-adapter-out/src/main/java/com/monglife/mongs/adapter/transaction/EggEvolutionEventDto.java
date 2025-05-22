package com.monglife.mongs.adapter.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EggEvolutionEventDto {

    private Long mongId;

    @Builder
    public EggEvolutionEventDto(Long mongId) {
        this.mongId = mongId;
    }
}
