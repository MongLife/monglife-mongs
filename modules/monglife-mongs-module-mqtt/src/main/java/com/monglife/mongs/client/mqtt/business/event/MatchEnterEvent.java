package com.monglife.mongs.client.mqtt.business.event;

import lombok.Builder;

@Builder
public record MatchEnterEvent(
        String roomId,
        String playerId
) {
}
