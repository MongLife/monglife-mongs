package com.monglife.mongs.app.battle.global.dto;

import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BattleRequestDto<T> {

    private BattleStateCode code;

    private T data;

    @Builder
    public BattleRequestDto(BattleStateCode code, T data) {
        this.code = code;
        this.data = data;
    }
}
