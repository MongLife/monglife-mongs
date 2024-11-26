package com.monglife.mongs.app.activity.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BattleStateCode {

    /**
     * 요청
     */
    BATTLE_ENTER("배틀에 입장했습니다.", "match"),
    BATTLE_EXIT("배틀에서 퇴장했습니다.", "match"),
    BATTLE_PICK("배틀 선택을 완료했습니다.", "match"),

    /**
     * 응답
     */
    BATTLE_CREATE_FAIL("배틀 매칭에 실패했습니다.", "search"),
    BATTLE_CREATE("배틀 매칭이 성사되었습니다.", "search"),
    BATTLE_FIGHT("배틀 라운드 선택이 완료되었습니다.", "match"),
    BATTLE_OVER("배틀이 종료되었습니다.", "match"),
    ;

    private final String message;

    private final String subTopic;
}
