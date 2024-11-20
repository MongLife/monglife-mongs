package com.monglife.mongs.app.battle.global.dto;

import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BattleResponseDto<T> {

    private BattleStateCode code;

    private List<String> topics;

    private T data;

    private Boolean isLastRound;

    @Builder
    public BattleResponseDto(BattleStateCode code, List<String> topics, T data, Boolean isLastRound) {
        this.code = code;
        this.topics = topics;
        this.data = data;
        this.isLastRound = isLastRound;
    }
}
