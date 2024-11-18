package com.monglife.mongs.app.battle.dto.etc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OverBattleDto {

    private String roomId;

    private Integer round;

    private String winPlayerId;

    private String losePlayerId;
}
