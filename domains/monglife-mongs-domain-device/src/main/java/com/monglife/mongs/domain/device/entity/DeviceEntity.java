package com.monglife.mongs.domain.device.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_device")
public class DeviceEntity extends BaseTimeEntity {

    @Id
    @Column(name = "device_id", unique = true)
    private String deviceId;

    @Column(name = "walking_count")
    private Integer walkingCount;

    @Column(name = "total_walking_count")
    private Integer totalWalkingCount;

    @Column(name = "consume_walking_count")
    private Integer consumeWalkingCount;

    @Column(name = "device_booted_dt")
    private LocalDateTime deviceBootedDt;

    @Setter
    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    private List<DeviceHistoryEntity> history = new ArrayList<>();

    @Builder
    public DeviceEntity(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt, String fcmToken) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.fcmToken = fcmToken;
    }

    public Integer getNowWalkingCount() {
        return this.walkingCount + (this.totalWalkingCount - this.consumeWalkingCount);
    }

    public void updateTotalWalkingCount(Integer totalWalkingCount) {

        if (this.totalWalkingCount.equals(totalWalkingCount)) return;

        this.totalWalkingCount = totalWalkingCount;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.TOTAL_WALKING_COUNT_UPDATE);
    }

    public void resetTotalWalkingCount(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        if (this.totalWalkingCount.equals(totalWalkingCount) && this.deviceBootedDt == deviceBootedDt) return;

        this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = 0;
        this.deviceBootedDt = deviceBootedDt;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.TOTAL_WALKING_COUNT_RESET);
    }

    public void decreaseWalkingCount(Integer walkingCount) {

        if (walkingCount <= 0) return;

        this.consumeWalkingCount = this.consumeWalkingCount + walkingCount;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.STEPS_DECREASE);
    }

    private void addHistory(DeviceHistoryEntity.DeviceHistoryType deviceHistoryType) {

        this.history.add(DeviceHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .fcmToken(this.fcmToken)
                .type(deviceHistoryType)
                .build());
    }
}
