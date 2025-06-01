package com.monglife.mongs.adapter.out.battle.publish.dto.response;

import com.monglife.mongs.adapter.out.battle.publish.vo.QueuePlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchingQueuePlayerPublishDto {

    private Long matchId;

    private List<QueuePlayerVo> matchPlayers;

    @Builder
    public MatchingQueuePlayerPublishDto(Long matchId, List<QueuePlayerVo> matchPlayers) {
        this.matchId = matchId;
        this.matchPlayers = matchPlayers;
    }
}
