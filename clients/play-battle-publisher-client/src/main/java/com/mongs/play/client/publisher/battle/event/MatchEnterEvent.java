package com.mongs.play.client.publisher.battle.event;

import lombok.Builder;

@Builder
public record MatchEnterEvent(
        String roomId,
        String playerId
) {
}
