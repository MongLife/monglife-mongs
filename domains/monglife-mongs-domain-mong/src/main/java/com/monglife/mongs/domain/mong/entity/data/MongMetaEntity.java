package com.monglife.mongs.domain.mong.entity.data;

import com.monglife.mongs.domain.mong.entity.history.MongMetaHistoryEntity;
import com.monglife.mongs.domain.mong.enums.MongMetaHistoryCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

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
    private List<MongMetaHistoryEntity> history;

    public MongMetaEntity() {
        this.trainingCount = 0;
        this.strokeCount = 0;
        this.reward = 0D;
        this.penalty = 0D;
        this.isActive = Boolean.TRUE;
    }

    public void increaseStrokeCount() {
        this.strokeCount = this.strokeCount + 1;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_INCREASE_STROKE_COUNT)
                .strokeCount(1)
                .build());
    }

    public void increaseTrainingCount() {
        this.trainingCount = this.trainingCount + 1;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_INCREASE_TRAINING_COUNT)
                .trainingCount(1)
                .build());
    }

    public void increasePenalty() {
        this.penalty = this.penalty + DEFAULT_PENALTY;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_INCREASE_PENALTY)
                .penalty(DEFAULT_PENALTY)
                .build());
    }

    public void deActivate() {

        if (!this.isActive) return;

        this.isActive = Boolean.FALSE;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_DEACTIVATE)
                .isActive(Boolean.FALSE)
                .build());
    }

    public void resetPenalty() {

        if (this.penalty == 0) return;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_RESET_PENALTY)
                .penalty(-this.penalty)
                .build());

        this.penalty = 0D;
    }

    public void setReward(Double reward) {

        this.reward = reward;

        this.history.add(MongMetaHistoryEntity.builder()
                .mongMetaHistoryCode(MongMetaHistoryCode.HISTORY_MONG_META_SET_REWARD)
                .penalty(-this.penalty)
                .build());
    }
}
