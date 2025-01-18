package com.monglife.mongs.domain.device.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "device_history_type")
    private DeviceHistoryType type;

    @Builder
    public DeviceHistoryEntity(Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt, DeviceHistoryType type) {
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.type = type;
    }

    /**
     * 변경 이력 코드
     */
    @Getter
    @AllArgsConstructor
    public enum DeviceHistoryType {

        TOTAL_WALKING_COUNT_UPDATE("총 걸음 수 갱신"),
        TOTAL_WALKING_COUNT_RESET("총 걸음 수 초기화"),
        WALKING_COUNT_DECREASE("보유 걸음 수 감소"),
        ;

        public final String name;
    }
}
