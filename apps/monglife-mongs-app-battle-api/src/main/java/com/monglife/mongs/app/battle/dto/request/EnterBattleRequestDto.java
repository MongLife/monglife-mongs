package com.monglife.mongs.app.battle.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterBattleRequestDto {

    private Long roomId;

    private String playerId;

    @Builder
    public EnterBattleRequestDto(Long roomId, String playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }
}
