package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_mong_meta_history")
public class MongMetaHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_meta_history_id")
    private Long mongMetaHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_meta_history_type")
    private MongMetaHistoryType mongMetaHistoryType;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Setter
    @Column(name = "reward")
    private Double reward;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public MongMetaHistoryEntity(MongMetaHistoryType mongMetaHistoryType, Integer trainingCount, Integer strokeCount, Double reward, Double penalty, Boolean isActive) {
        this.mongMetaHistoryType = mongMetaHistoryType;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.reward = reward;
        this.penalty = penalty;
        this.isActive = isActive;
    }

    @Getter
    @AllArgsConstructor
    public enum MongMetaHistoryType {

        HISTORY_MONG_META_INCREASE_STROKE_COUNT("쓰다듬기 횟수 증가"),
        HISTORY_MONG_META_INCREASE_TRAINING_COUNT("훈련 횟수 증가"),
        HISTORY_MONG_META_INCREASE_PENALTY("진화 패널티 증가"),
        HISTORY_MONG_META_DEACTIVATE("비활성화"),
        HISTORY_MONG_META_SET_REWARD("진화 리워드 수정"),
        HISTORY_MONG_META_RESET_PENALTY("진화 패널티 초기화"),
        ;

        public final String name;
    }
}
