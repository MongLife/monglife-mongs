package com.monglife.mongs.client.mqtt.business.vo.res;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MatchVo(
        String roomId,
        Integer round,
        List<MatchPlayerVo> matchPlayerVoList
) {
}
