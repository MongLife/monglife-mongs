package com.monglife.mongs.app.activity.battle.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@ToString
@Getter
@NoArgsConstructor
public class CreateBattleDto {

    private Long roomId;

    private Set<MatchPlayerVo> matchPlayers;

    @Builder
    public CreateBattleDto(Long roomId, Set<MatchPlayerVo> matchPlayers) {
        this.roomId = roomId;
        this.matchPlayers = matchPlayers;
    }

    public static CreateBattleDto of(MatchVo matchVo) {
        return CreateBattleDto.builder()
                .roomId(matchVo.getRoomId())
                .matchPlayers(matchVo.getMatchPlayers())
                .build();
    }
}
