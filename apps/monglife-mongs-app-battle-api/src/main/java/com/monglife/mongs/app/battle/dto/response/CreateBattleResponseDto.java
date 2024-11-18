package com.monglife.mongs.app.battle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleResponseDto {

    private String roomId;

    private String playerId;

    private String riverMongCode;

    @Builder
    public CreateBattleResponseDto(String roomId, String playerId, String riverMongCode) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.riverMongCode = riverMongCode;
    }
}
