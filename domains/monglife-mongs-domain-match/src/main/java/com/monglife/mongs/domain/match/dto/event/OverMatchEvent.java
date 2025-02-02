package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OverMatchEvent {

    private Long roomId;

    private Long winMongId;

    private String winPlayerId;

    private String winMongTypeCode;

    @Builder
    public OverMatchEvent(Long roomId, Long winMongId, String winPlayerId, String winMongTypeCode) {
        this.roomId = roomId;
        this.winMongId = winMongId;
        this.winPlayerId = winPlayerId;
        this.winMongTypeCode = winMongTypeCode;
    }

    public static OverMatchEvent of (MatchRoomEntity matchRoomEntity, MatchPlayerEntity winMatchPlayerEntity) {
        return OverMatchEvent.builder()
                .roomId(matchRoomEntity.getRoomId())
                .winMongId(winMatchPlayerEntity.getMongId())
                .winPlayerId(winMatchPlayerEntity.getPlayerId())
                .winMongTypeCode(winMatchPlayerEntity.getMongTypeCode())
                .build();
    }
}
