package com.monglife.mongs.app.battle.dto.request;

import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BattleRequestDto<T> {

    private BattleStateCode code;

    private T data;

    private LocalDateTime createdDt;

    @Builder
    public BattleRequestDto(BattleStateCode code, T data) {
        this.code = code;
        this.data = data;
        this.createdDt = LocalDateTime.now();
    }
}
