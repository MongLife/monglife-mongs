package com.monglife.mongs.app.activity.battle.dto.request;

import com.monglife.mongs.app.activity.battle.enums.BattleStateCode;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BattleRequestDto<T> {

    private BattleStateCode code;

    private String callbackTopic;

    private T data;

    @Builder
    public BattleRequestDto(BattleStateCode code, String callbackTopic, T data) {
        this.code = code;
        this.callbackTopic = callbackTopic;
        this.data = data;
    }
}
