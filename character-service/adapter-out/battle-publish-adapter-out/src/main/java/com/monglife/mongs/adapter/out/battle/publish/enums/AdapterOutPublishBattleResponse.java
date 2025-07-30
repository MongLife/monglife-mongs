package com.monglife.mongs.adapter.out.battle.publish.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishBattleResponse implements Response {

    BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER(HttpStatus.OK.value(), "MONGS-CHARACTER-BATTLE-500", "배틀 매칭에 성공했습니다."),
    BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER_FAIL(HttpStatus.NOT_ACCEPTABLE.value(), "MONGS-CHARACTER-BATTLE-501", "배틀 매칭에 실패했습니다."),
    BATTLE_PUBLISH_MATCH_PLAYERS_ENTERED(HttpStatus.OK.value(), "MONGS-CHARACTER-BATTLE-502", "모든 플레이어가 입장했습니다."),
    BATTLE_PUBLISH_MATCH(HttpStatus.OK.value(), "MONGS-CHARACTER-BATTLE-503", "라운드가 종료되었습니다."),
    BATTLE_PUBLISH_MATCH_END(HttpStatus.OK.value(), "MONGS-CHARACTER-BATTLE-504", "배틀이 종료되었습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
