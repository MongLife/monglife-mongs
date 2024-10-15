package com.monglife.mongs.client.mqtt.business.vo.req;

import lombok.Builder;

@Builder
public record MatchExitVo(
        String roomId,
        String playerId
) {
}
