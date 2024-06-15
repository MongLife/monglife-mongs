package com.mongs.play.client.publisher.battle.event;

import lombok.Builder;

@Builder
public record MatchPickEvent(
        String roomId,
        String playerId,
        String targetPlayerId,
        String pick
) {
}
