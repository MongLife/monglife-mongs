package com.monglife.mongs.domain.member.entity;

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
@Table(name = "mongs_member_step_history")
public class MemberStepHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_step_history_id")
    private Long memberStepHistoryId;

    @Column(name = "walking_count")
    private Integer walkingCount;

    @Column(name = "total_walking_count")
    private Integer totalWalkingCount;

    @Column(name = "consume_walking_count")
    private Integer consumeWalkingCount;

    @Column(name = "device_booted_dt")
    private LocalDateTime deviceBootedDt;

    @Column(name = "member_step_history_type")
    private MemberStepHistoryType type;

    @Builder
    public MemberStepHistoryEntity(Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt, MemberStepHistoryType type) {
        this.walkingCount = walkingCount == null ? 0 : walkingCount;
        this.totalWalkingCount = totalWalkingCount == null ? 0 : totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount == null ? 0 : consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
        this.type = type;
    }

    public enum MemberStepHistoryType {
        UPDATE,
        RESET,
        DECREASE,
        CREATE
    }
}
