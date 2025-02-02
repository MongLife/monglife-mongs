package com.monglife.mongs.domain.match.vo;

import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OverMatchVo {

    private final String playerId;

    private final Long mongId;

    private final String mongTypeCode;

    private final Boolean isBot;

    @Builder
    public OverMatchVo(String playerId, Long mongId, String mongTypeCode, Boolean isBot) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.isBot = isBot;
    }

    public static OverMatchVo of(MatchPlayerEntity matchPlayerEntity) {
        return OverMatchVo.builder()
                .playerId(matchPlayerEntity.getPlayerId())
                .mongId(matchPlayerEntity.getMongId())
                .mongTypeCode(matchPlayerEntity.getMongTypeCode())
                .isBot(matchPlayerEntity.getIsBot())
                .build();
    }
}
