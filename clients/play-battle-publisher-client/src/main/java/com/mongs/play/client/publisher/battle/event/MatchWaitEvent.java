package com.mongs.play.client.publisher.battle.event;

import lombok.Builder;

@Builder
public record MatchWaitEvent(
        Long mongId
) {
}
