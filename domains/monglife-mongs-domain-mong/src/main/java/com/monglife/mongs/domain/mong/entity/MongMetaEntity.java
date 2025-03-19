package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongMetaHistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_mong_meta")
@ToString(exclude = { "history", "mong" })
public class MongMetaEntity {

    private static final Double DEFAULT_PENALTY = 0.3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_meta_id")
    private Long mongMetaId;

    @OneToOne(mappedBy = "meta", cascade = CascadeType.PERSIST)
    private MongEntity mong;

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
    @JoinColumn(name = "mong_meta_id")
    private List<MongMetaHistoryEntity> history = new ArrayList<>();

//    public MongMetaEntity(MongEntity mong) {
//        this.mong = mong;
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

        this.addHistory(MongMetaHistoryType.INCREASE_STROKE_COUNT);
    }

    /**
     * 쓰다듬 횟수 초기화
     */
    public void resetStrokeCount() {

        if (this.strokeCount == 0) return;

        this.strokeCount = 0;

        this.addHistory(MongMetaHistoryType.RESET_STROKE_COUNT);
    }

    /**
     * 훈련 횟수 증가
     */
    public void increaseTrainingCount() {

        this.trainingCount = this.trainingCount + 1;

        this.addHistory(MongMetaHistoryType.INCREASE_TRAINING_COUNT);
    }

    /**
     * 훈련 횟수 초기화
     */
    public void resetTrainingCount() {

        if (this.trainingCount == 0) return;

        this.trainingCount = 0;

        this.addHistory(MongMetaHistoryType.RESET_TRAINING_COUNT);
    }

    /**
     * 패널티 증가
     */
    public void increasePenalty() {

        this.penalty = this.penalty + DEFAULT_PENALTY;

        this.addHistory(MongMetaHistoryType.INCREASE_PENALTY);
    }

    /**
     * 패널티 초기화
     */
    public void resetPenalty() {

        if (this.penalty == 0D) return;

        this.penalty = 0D;

        this.addHistory(MongMetaHistoryType.RESET_PENALTY);
    }

    /**
     * 리워드 설정
     * @param reward 리워드
     */
    public void setReward(Double reward) {

        if (this.reward.equals(reward)) return;

        this.reward = reward;

        this.addHistory(MongMetaHistoryType.SET_REWARD);
    }

    /**
     * 비활성화
     */
    public void deActivate() {

        if (!this.isActive) return;

        this.isActive = Boolean.FALSE;

        this.addHistory(MongMetaHistoryType.DEACTIVATE);
    }

    private void addHistory(MongMetaHistoryType mongMetaHistoryType) {

        this.history.add(MongMetaHistoryEntity.builder()
                .mongId(this.mong.getMongId())
                .accountId(this.mong.getAccountId())
                .mongName(this.mong.getMongName())
                .mongMetaHistoryType(mongMetaHistoryType)
                .trainingCount(this.trainingCount)
                .strokeCount(this.strokeCount)
                .reward(this.reward)
                .penalty(this.penalty)
                .isActive(this.isActive)
                .build());
    }
}
