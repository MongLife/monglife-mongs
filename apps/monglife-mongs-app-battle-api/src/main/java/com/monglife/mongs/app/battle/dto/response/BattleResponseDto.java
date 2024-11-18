package com.monglife.mongs.app.battle.dto.response;

import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BattleResponseDto<T> {

    private BattleStateCode code;

    private T data;

    private LocalDateTime createdDt;

    @Builder
    public BattleResponseDto(BattleStateCode code, T data) {
        this.code = code;
        this.data = data;
        this.createdDt = LocalDateTime.now();
    }
}
