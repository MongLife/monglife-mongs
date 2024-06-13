package com.mongs.play.client.publisher.battle.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
public record MatchWaitVo(
        Long mongId
) {
}
