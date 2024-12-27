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
@Table(name = "mongs_step")
public class StepEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long stepId;

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
    @JoinColumn(name = "step_id")
    private List<StepHistoryEntity> history;

    @Builder
    public StepEntity(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.history = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {

        this.history.add(StepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(StepHistoryEntity.StepHistoryType.CREATE)
                .build());
    }

    public Integer getNowWalkingCount() {
        return walkingCount + (totalWalkingCount - consumeWalkingCount);
    }

    public void updateTotalWalkingCount(Integer totalWalkingCount) {

        this.totalWalkingCount = totalWalkingCount;

        this.history.add(StepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(StepHistoryEntity.StepHistoryType.UPDATE)
                .build());
    }

    public void resetTotalWalkingCount(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = 0;
        this.deviceBootedDt = deviceBootedDt;

        this.history.add(StepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(StepHistoryEntity.StepHistoryType.RESET)
                .build());
    }

    public void decreaseWalkingCount(Integer walkingCount) {

        this.consumeWalkingCount = this.consumeWalkingCount + walkingCount;

        this.history.add(StepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(StepHistoryEntity.StepHistoryType.DECREASE)
                .build());
    }
}
