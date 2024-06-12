package com.mongs.play.client.publisher.battle.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchOverVo {
    private String roomId;
    private Long nameA;
    private String mongCodeA;
    private Long nameB;
    private String mongCodeB;
}
