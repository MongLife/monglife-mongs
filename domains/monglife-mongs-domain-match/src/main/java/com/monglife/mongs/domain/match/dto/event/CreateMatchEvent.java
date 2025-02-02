package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class CreateMatchEvent {

    private Long roomId;

    private Set<MatchPlayerVo> matchPlayers;

    @Builder
    public CreateMatchEvent(Long roomId, Set<MatchPlayerVo> matchPlayers) {
        this.roomId = roomId;
        this.matchPlayers = matchPlayers;
    }


}
