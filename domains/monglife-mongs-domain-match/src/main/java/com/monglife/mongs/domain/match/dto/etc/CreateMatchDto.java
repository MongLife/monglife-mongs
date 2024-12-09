package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateMatchDto {

    private Long roomId;

    private Set<MatchPlayerVo> matchPlayers;

    @Builder
    public CreateMatchDto(Long roomId, Set<MatchPlayerVo> matchPlayers) {
        this.roomId = roomId;
        this.matchPlayers = matchPlayers;
    }
}
