package com.mongs.play.app.battle.worker.vo;

import lombok.Builder;

@Builder
public class BattlePlayerVo {
    private String mongId;
    private String mongCode;
    private Boolean direction;      // true: left, false: right

    @Builder.Default
    private Double hp = 500D;
    @Builder.Default
    private Double weight = 0D;
    @Builder.Default
    private Double strength = 100D;
    @Builder.Default
    private Double sleep = 100D;

    @Builder.Default
    private Boolean isCom = true;
}
