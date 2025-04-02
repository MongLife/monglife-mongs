package com.mongs.play.app.battle.worker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundResultVo {
    private String playerId;
    private double damage;
    private boolean isAttacked;
    private boolean isDefenced;
    private boolean isHealed;
}
