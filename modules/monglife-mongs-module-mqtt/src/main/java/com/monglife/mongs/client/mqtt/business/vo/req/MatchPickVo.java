package com.monglife.mongs.client.mqtt.business.vo.req;

import lombok.Builder;

@Builder
public record MatchPickVo(
        String roomId,
        String playerId,
        String targetPlayerId,
        String pick
) {
}
