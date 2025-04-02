package com.monglife.mongs.app.activity.battle.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterBattleRequestDto {

    private String playerId;

    @Builder
    public EnterBattleRequestDto(String playerId) {
        this.playerId = playerId;
    }
}
