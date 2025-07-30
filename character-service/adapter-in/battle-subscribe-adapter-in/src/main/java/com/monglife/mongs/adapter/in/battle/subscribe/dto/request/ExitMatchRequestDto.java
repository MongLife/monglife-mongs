package com.monglife.mongs.adapter.in.battle.subscribe.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExitMatchRequestDto {

    private String playerId;

    @Builder
    public ExitMatchRequestDto(String playerId) {
        this.playerId = playerId;
    }
}
