package com.monglife.mongs.adapter.out.battle.persistence.entity;

import com.monglife.mongs.domain.battle.model.QueuePlayer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class QueuePlayerEntity {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private LocalDateTime createdAt;

    @Builder
    public QueuePlayerEntity(Long mongId, String deviceId, Long accountId, LocalDateTime createdAt) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.createdAt = createdAt;
    }

    public QueuePlayer toDomain() {
        return QueuePlayer.builder()
                .mongId(this.mongId)
                .deviceId(this.deviceId)
                .accountId(this.accountId)
                .build();
    }
}
