package com.monglife.mongs.adapter.out.battle.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class QueuePlayerEntity {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    @Builder
    public QueuePlayerEntity(Long mongId, String deviceId, Long accountId) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
    }
}
