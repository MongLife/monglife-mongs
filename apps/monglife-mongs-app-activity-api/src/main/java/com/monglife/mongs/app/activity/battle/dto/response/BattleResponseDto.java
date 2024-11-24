package com.monglife.mongs.app.activity.battle.dto.response;

import com.monglife.mongs.app.activity.battle.enums.BattleStateCode;
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

    @Builder
    public BattleResponseDto(BattleStateCode code, List<String> topics, T data) {
        this.code = code;
        this.topics = topics;
        this.data = data;
    }
}
