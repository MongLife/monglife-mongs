package com.monglife.mongs.app.battle.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingEntity {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;
}
