package com.monglife.mongs.domain.device.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_device_history")
public class DeviceHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_history_id")
    private Long deviceHistoryId;

    @Column(name = "walking_count")
    private Integer walkingCount;

    @Column(name = "total_walking_count")
    private Integer totalWalkingCount;

    @Column(name = "consume_walking_count")
    private Integer consumeWalkingCount;

    @Column(name = "device_booted_dt")
    private LocalDateTime deviceBootedDt;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_history_type")
    private DeviceHistoryType type;

    @Builder
    public DeviceHistoryEntity(Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt, DeviceHistoryType type, String fcmToken) {
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.fcmToken = fcmToken;
        this.type = type;
    }

    public enum DeviceHistoryType {
        TOTAL_WALKING_COUNT_UPDATE,
        TOTAL_WALKING_COUNT_RESET,
        STEPS_DECREASE,
        FCM_TOKEN_UPDATE,
    }
}
