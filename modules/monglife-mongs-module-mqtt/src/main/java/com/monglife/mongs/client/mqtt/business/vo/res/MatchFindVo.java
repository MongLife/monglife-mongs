package com.monglife.mongs.client.mqtt.business.vo.res;

import lombok.Builder;

@Builder
public record MatchFindVo(
        String roomId,
        String playerId
) {
}
