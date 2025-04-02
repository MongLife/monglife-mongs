package com.monglife.mongs.domain.match.vo;

import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MatchVo {

    private final Long roomId;

    private final Integer round;

    private final Set<MatchPlayerVo> matchPlayers;

    private final Boolean isLastRound;

    @Builder
    public MatchVo(Long roomId, Integer round, Set<MatchPlayerVo> matchPlayers, Boolean isLastRound) {
        this.roomId = roomId;
        this.round = round;
        this.matchPlayers = matchPlayers;
        this.isLastRound = isLastRound;
    }

    public static MatchVo of(MatchRoomEntity matchRoomEntity) {
        return MatchVo.builder()
                .roomId(matchRoomEntity.getRoomId())
                .round(matchRoomEntity.getRound())
                .matchPlayers(matchRoomEntity.getMatchPlayerSet().stream()
                        .map(MatchPlayerVo::of)
                        .collect(Collectors.toSet()))
                .isLastRound(matchRoomEntity.isLastRound())
                .build();
    }
}
