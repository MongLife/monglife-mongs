package com.monglife.mongs.adapter.in.battle.subscribe.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class EnterMatchRequestDto {

    private String playerId;

    @Builder
    public EnterMatchRequestDto(String playerId) {
        this.playerId = playerId;
    }
}
