package com.monglife.mongs.client.mqtt.business.event;

import lombok.Builder;

@Builder
public record MatchExitEvent(
        String roomId,
        String playerId
) {
}
