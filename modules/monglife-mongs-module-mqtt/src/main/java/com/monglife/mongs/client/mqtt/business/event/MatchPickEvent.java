package com.monglife.mongs.client.mqtt.business.event;

import lombok.Builder;

@Builder
public record MatchPickEvent(
        String roomId,
        String playerId,
        String targetPlayerId,
        String pick
) {
}
