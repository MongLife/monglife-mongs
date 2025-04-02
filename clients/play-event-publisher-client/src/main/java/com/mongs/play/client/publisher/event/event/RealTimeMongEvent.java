package com.mongs.play.client.publisher.event.event;

import lombok.Builder;

import java.util.List;

@Builder
public record RealTimeMongEvent(
        Long mongId,
        List<Object> publishVoList
) {
}
