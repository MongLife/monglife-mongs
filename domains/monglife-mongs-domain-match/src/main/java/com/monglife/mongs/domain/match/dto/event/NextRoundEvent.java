package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class NextRoundEvent {

    private Long roomId;

    private Integer round;

    private Boolean isLastRound;

    private Set<MatchPlayerVo> battlePlayers;

    @Builder
    public NextRoundEvent(Long roomId, Integer round, Boolean isLastRound, Set<MatchPlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.round = round;
        this.isLastRound = isLastRound;
        this.battlePlayers = battlePlayers;
    }

    public static NextRoundEvent of (MatchRoomEntity matchRoomEntity) {
        return NextRoundEvent.builder()
                .roomId(matchRoomEntity.getRoomId())
                .round(matchRoomEntity.getRound())
                .isLastRound(matchRoomEntity.isLastRound())
                .battlePlayers(matchRoomEntity.getMatchPlayerSet().stream()
                        .map(MatchPlayerVo::of)
                        .collect(Collectors.toSet()))
                .build();
    }
}
