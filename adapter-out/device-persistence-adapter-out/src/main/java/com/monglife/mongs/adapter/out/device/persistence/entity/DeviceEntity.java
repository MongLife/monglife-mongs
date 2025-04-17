package com.monglife.mongs.adapter.out.device.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.model.Step;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Builder
    public DeviceEntity(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }

    /**
     * 걸음 수 도메인을 기반으로 엔티티 수정
     * @param step 걸음 수 도메인 객체
     */
    public void update(Step step) {
        this.walkingCount = step.getWalkingCount();
        this.totalWalkingCount = step.getTotalWalkingCount();
        this.consumeWalkingCount = step.getConsumeWalkingCount();
        this.deviceBootedDt = step.getDeviceBootedDt();
    }

    /**
     * 엔티티 도메인 변환
     * @return 걸음 수 도메인 객체
     */
    public Step toDomain() {
        return Step.builder()
                .deviceId(this.deviceId)
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .build();
    }
}
