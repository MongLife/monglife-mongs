package com.mongs.play.client.publisher.battle.vo.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record MatchPlayerVo(
        String playerId,
        String mongCode,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Double hp,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String state
) {
}
