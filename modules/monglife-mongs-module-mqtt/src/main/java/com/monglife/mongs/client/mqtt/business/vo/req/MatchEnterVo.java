package com.monglife.mongs.client.mqtt.business.vo.req;

import lombok.Builder;

@Builder
public record MatchEnterVo(
        String roomId,
        String playerId
) {
}
