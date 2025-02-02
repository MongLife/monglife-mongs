package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class EnterMatchEvent {

    private Long roomId;

    private Integer round;

    private Boolean isLastRound;

    private Set<MatchPlayerVo> battlePlayers;

    @Builder
    public EnterMatchEvent(Long roomId, Integer round, Boolean isLastRound, Set<MatchPlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.round = round;
        this.isLastRound = isLastRound;
        this.battlePlayers = battlePlayers;
    }

    public static EnterMatchEvent of (MatchRoomEntity matchRoomEntity) {

        Set<MatchPlayerVo> matchPlayers = matchRoomEntity.getMatchPlayerSet().stream()
                .map(matchPlayerEntity -> MatchPlayerVo.of(matchPlayerEntity, MatchRoundCode.NONE))
                .collect(Collectors.toSet());

        return EnterMatchEvent.builder()
                .roomId(matchRoomEntity.getRoomId())
                .round(0)
                .isLastRound(matchRoomEntity.isLastRound())
                .battlePlayers(matchPlayers)
                .build();
    }
}
