package com.monglife.mongs.domain.match.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OverMatchVo {

    private final String playerId;

    private final Long mongId;

    private final String mongTypeCode;

    @Builder
    public OverMatchVo(String playerId, Long mongId, String mongTypeCode) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
    }
}
