package com.monglife.mongs.domain.member.entity;

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
@Table(name = "mongs_member_step")
public class MemberStepEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_step_id")
    private Long memberStepId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private MemberEntity member;

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
    @JoinColumn(name = "member_step_id")
    private List<MemberStepHistoryEntity> history;

    @Builder
    public MemberStepEntity(MemberEntity member, String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.member = member;
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.history = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {

        this.history.add(MemberStepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(MemberStepHistoryEntity.MemberStepHistoryType.CREATE)
                .build());
    }

    public Integer getNowWalkingCount() {
        return walkingCount + (totalWalkingCount - consumeWalkingCount);
    }

    public void updateTotalWalkingCount(Integer totalWalkingCount) {

        this.totalWalkingCount = totalWalkingCount;

        this.history.add(MemberStepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(MemberStepHistoryEntity.MemberStepHistoryType.UPDATE)
                .build());
    }

    public void resetTotalWalkingCount(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = 0;
        this.deviceBootedDt = deviceBootedDt;

        this.history.add(MemberStepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(MemberStepHistoryEntity.MemberStepHistoryType.RESET)
                .build());
    }

    public void decreaseWalkingCount(Integer walkingCount) {

        this.consumeWalkingCount = this.consumeWalkingCount + walkingCount;

        this.history.add(MemberStepHistoryEntity.builder()
                .walkingCount(this.walkingCount)
                .totalWalkingCount(this.totalWalkingCount)
                .consumeWalkingCount(this.consumeWalkingCount)
                .deviceBootedDt(this.deviceBootedDt)
                .type(MemberStepHistoryEntity.MemberStepHistoryType.DECREASE)
                .build());
    }
}
