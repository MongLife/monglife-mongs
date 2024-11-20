package com.monglife.mongs.app.battle.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BattleStateCode {

    /**
     * 요청
     */
    BATTLE_ENTER("배틀 입장", "match"),
    BATTLE_EXIT("배틀 퇴장", "match"),
    BATTLE_PICK("배틀 선택", "match"),

    /**
     * 응답
     */
    BATTLE_CREATE("배틀 생성", "search"),
    BATTLE_FIGHT("배틀 선택 완료", "match"),
    BATTLE_OVER("배틀 종료", "match"),
    ;

    private final String message;

    private final String subTopic;
}
