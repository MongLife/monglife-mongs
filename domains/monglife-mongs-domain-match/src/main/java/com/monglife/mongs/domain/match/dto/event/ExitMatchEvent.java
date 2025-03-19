package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExitMatchEvent {

    private Long roomId;

    private Long winMongId;

    private String winPlayerId;

    private String winMongTypeCode;

    private Boolean isBot;

    @Builder
    public ExitMatchEvent(Long roomId, Long winMongId, String winPlayerId, String winMongTypeCode, Boolean isBot) {
        this.roomId = roomId;
        this.winMongId = winMongId;
        this.winPlayerId = winPlayerId;
        this.winMongTypeCode = winMongTypeCode;
        this.isBot = isBot;
    }

    public static ExitMatchEvent of(Long roomId, MatchPlayerEntity winMatchPlayerEntity) {
        return ExitMatchEvent.builder()
                .roomId(roomId)
                .winMongId(winMatchPlayerEntity.getMongId())
                .winPlayerId(winMatchPlayerEntity.getPlayerId())
                .winMongTypeCode(winMatchPlayerEntity.getMongTypeCode())
                .isBot(winMatchPlayerEntity.getIsBot())
                .build();
    }
}
