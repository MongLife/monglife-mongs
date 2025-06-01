package com.monglife.mongs.adapter.in.battle.subscribe.dto.request;

import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickMatchRequestDto {

    private String playerId;

    private String targetPlayerId;

    private MatchPickCode pickCode;

    @Builder
    public PickMatchRequestDto(String playerId, String targetPlayerId, MatchPickCode pickCode) {
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
