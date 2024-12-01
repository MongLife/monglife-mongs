package com.monglife.mongs.app.activity.battle.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EnterBattleRequestDto {

    private String playerId;

    @Builder
    public EnterBattleRequestDto(String playerId) {
        this.playerId = playerId;
    }
}
