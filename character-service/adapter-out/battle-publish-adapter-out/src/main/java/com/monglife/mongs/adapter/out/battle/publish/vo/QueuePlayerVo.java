package com.monglife.mongs.adapter.out.battle.publish.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueuePlayerVo {

    private final String deviceId;

    private final String playerId;

    private final Long mongId;

    private final String mongTypeCode;

    private final String mongTypeName;

    @Builder
    public QueuePlayerVo(String deviceId, String playerId, Long mongId, String mongTypeCode, String mongTypeName) {
        this.deviceId = deviceId;
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
    }
}
