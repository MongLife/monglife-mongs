package com.monglife.mongs.app.activity.battle.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleDto {

    private Long roomId;

    private Set<MatchPlayerVo> battlePlayers;

    @Builder
    public CreateBattleDto(Long roomId, Set<MatchPlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.battlePlayers = battlePlayers;
    }
}
