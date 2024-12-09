package com.monglife.mongs.app.activity.battle.dto.response;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleResponseDto {

    private Long roomId;

    private Set<MatchPlayerVo> battlePlayers;

    @Builder
    public CreateBattleResponseDto(Long roomId, Set<MatchPlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.battlePlayers = battlePlayers;
    }
}
