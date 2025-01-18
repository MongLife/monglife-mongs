package com.monglife.mongs.domain.device.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    private List<DeviceHistoryEntity> history = new ArrayList<>();

    @Builder
    public DeviceEntity(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }

    /**
     * 현재 보유한 걸음 수 조회
     * @return 현재 보유한 걸음 수
     */
    public Integer getNowWalkingCount() {
        return this.walkingCount + (this.totalWalkingCount - this.consumeWalkingCount);
    }

    /**
     * 총 걸음 수 갱신
     * @param totalWalkingCount 총 걸음 수 (기기 상 총 걸음 수)
     */
    public void updateTotalWalkingCount(Integer totalWalkingCount) {

        if (this.totalWalkingCount.equals(totalWalkingCount)) return;

        this.totalWalkingCount = totalWalkingCount;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.TOTAL_WALKING_COUNT_UPDATE);
    }

    /**
     * 총 걸음 수 초기화
     * @param totalWalkingCount 총 걸음 수 (기기 상 총 걸음 수)
     * @param deviceBootedDt 기기 부팅 시간
     */
    public void resetTotalWalkingCount(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        if (this.totalWalkingCount.equals(totalWalkingCount) && this.deviceBootedDt == deviceBootedDt) return;

        this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = 0;
        this.deviceBootedDt = deviceBootedDt;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.TOTAL_WALKING_COUNT_RESET);
    }

    /**
     * 보유한 걸음 수 감소
     * @param walkingCount 감소할 걸음 수
     */
    public void decreaseWalkingCount(Integer walkingCount) {

        if (walkingCount <= 0) return;

        this.consumeWalkingCount = this.consumeWalkingCount + walkingCount;

        this.addHistory(DeviceHistoryEntity.DeviceHistoryType.WALKING_COUNT_DECREASE);
    }

    /**
     * 변경 이력 저장
     * @param deviceHistoryType 변경 이력 코드
     */
    private void addHistory(DeviceHistoryEntity.DeviceHistoryType deviceHistoryType) {

        this.history.add(DeviceHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(deviceHistoryType)
                .build());
    }
}
