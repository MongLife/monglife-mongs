package com.monglife.mongs.domain.mong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@ToString(exclude = { "history" })
public class MongMetaEntity {

    private static final Double DEFAULT_PENALTY = 0.3;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Column(name = "reward")
    private Double reward;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_id")
    private List<MongMetaHistoryEntity> history = new ArrayList<>();

    public MongMetaEntity() {
        this.trainingCount = 0;
        this.strokeCount = 0;
        this.reward = 0D;
        this.penalty = 0D;
        this.isActive = Boolean.TRUE;
    }

    /**
     * 쓰다듬 횟수 증가
     */
    public void increaseStrokeCount() {

        this.strokeCount = this.strokeCount + 1;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_INCREASE_STROKE_COUNT);
    }

    public void resetStrokeCount() {

        if (this.strokeCount == 0) return;

        this.strokeCount = 0;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_RESET_STROKE_COUNT);
    }

    /**
     * 훈련 횟수 증가
     */
    public void increaseTrainingCount() {

        this.trainingCount = this.trainingCount + 1;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_INCREASE_TRAINING_COUNT);
    }

    public void resetTrainingCount() {

        if (this.trainingCount == 0) return;

        this.trainingCount = 0;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_RESET_TRAINING_COUNT);
    }

    /**
     * 패널티 증가
     */
    public void increasePenalty() {

        this.penalty = this.penalty + DEFAULT_PENALTY;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_INCREASE_PENALTY);
    }

    /**
     * 패널티 초기화
     */
    public void resetPenalty() {

        if (this.penalty == 0D) return;

        this.penalty = 0D;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_RESET_PENALTY);
    }

    /**
     * 리워드 설정
     * @param reward 리워드
     */
    public void setReward(Double reward) {

        if (this.reward.equals(reward)) return;

        this.reward = reward;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_SET_REWARD);
    }

    /**
     * 비활성화
     */
    public void deActivate() {

        if (!this.isActive) return;

        this.isActive = Boolean.FALSE;

        this.addHistory(MongMetaHistoryEntity.MongMetaHistoryType.HISTORY_MONG_META_DEACTIVATE);
    }

    private void addHistory(MongMetaHistoryEntity.MongMetaHistoryType mongMetaHistoryType) {

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryType(mongMetaHistoryType)
                .trainingCount(this.trainingCount)
                .strokeCount(this.strokeCount)
                .reward(this.reward)
                .penalty(this.penalty)
                .isActive(this.isActive)
                .build());
    }
}
