package com.monglife.mongs.domain.mong.entity.data;

import com.monglife.mongs.domain.mong.entity.history.MongStatusHistoryEntity;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.enums.MongStatusHistoryCode;
import com.monglife.mongs.domain.mong.listener.MongStatusEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(MongStatusEntityListener.class)
@Table(name = "mongs_mong_status")
@ToString(exclude = "mong")
public class MongStatusEntity {

    public static final Integer MAX_POOP_COUNT = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_id")
    private Long mongStatusId;

    @OneToOne(mappedBy = "status")
    @JoinColumn(name = "mong_status_id")
    private MongEntity mong;

    @Column(name = "max_status")
    private Double maxStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_status_code")
    private MongStatusCode code;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "poop_count")
    private Integer poopCount;

    @Column(name = "exp")
    private Double exp;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "healthy")
    private Double healthy;

    @Column(name = "fatigue")
    private Double fatigue;

    @Column(name = "exp_ratio")
    private Double expRatio;

    @Column(name = "strength_ratio")
    private Double strengthRatio;

    @Column(name = "satiety_ratio")
    private Double satietyRatio;

    @Column(name = "healthy_ratio")
    private Double healthyRatio;

    @Column(name = "fatigue_ratio")
    private Double fatigueRatio;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_status_id")
    private List<MongStatusHistoryEntity> history;

    public MongStatusEntity(Double maxStatus) {

        this.maxStatus = maxStatus;
        this.code = MongStatusCode.NORMAL;
        this.weight = 0D;
        this.poopCount = 0;

        this.exp = 0D;
        this.strength = maxStatus;
        this.satiety = maxStatus;
        this.healthy = maxStatus;
        this.fatigue = maxStatus;

        this.expRatio = 0D;
        this.strengthRatio = 100D;
        this.satietyRatio = 100D;
        this.healthyRatio = 100D;
        this.fatigueRatio = 100D;
    }

    public void increaseExp(Double addExp) {
        this.exp = this.exp + Math.abs(addExp);

        if (Math.abs(addExp) == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_EXP)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void decreaseExp(Double subExp) {
        this.exp = this.exp - Math.abs(subExp);

        if (Math.abs(subExp) == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_DECREASE_EXP)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void resetExp() {
        this.exp = 0D;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_RESET_EXP)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void increasePoopCount(Integer addPoopCount) {
        this.poopCount = this.poopCount + Math.abs(addPoopCount);

        if (Math.abs(addPoopCount) == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_POOP_COUNT)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void decreasePoopCount(Integer subPoopCount) {
        this.poopCount = this.poopCount - Math.abs(subPoopCount);

        if (Math.abs(subPoopCount) == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_DECREASE_POOP_COUNT)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void resetPoopCount() {
        this.poopCount = 0;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_RESET_POOP_COUNT)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void increaseWeight(Double addWeight) {
        this.weight = this.weight + Math.abs(addWeight);

        if (addWeight == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_WEIGHT)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void decreaseWeight(Double subWeight) {
        this.weight = this.weight - Math.abs(subWeight);

        if (subWeight == 0) return;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_DECREASE_WEIGHT)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void increaseStatus(Double strength, Double satiety, Double healthy, Double fatigue) {
        this.strength += Math.abs(strength);
        this.satiety += Math.abs(satiety);
        this.healthy += Math.abs(healthy);
        this.fatigue += Math.abs(fatigue);
        this.sinkStatusValueToStatusRatio();

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_STATUS)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void decreaseStatus(Double strength, Double satiety, Double healthy, Double fatigue) {
        this.strength -= Math.abs(strength);
        this.satiety -= Math.abs(satiety);
        this.healthy -= Math.abs(healthy);
        this.fatigue -= Math.abs(fatigue);
        this.sinkStatusValueToStatusRatio();

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_DECREASE_STATUS)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void increaseStatusRatio(Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {
        this.strengthRatio += Math.abs(strengthRatio);
        this.satietyRatio += Math.abs(satietyRatio);
        this.healthyRatio += Math.abs(healthyRatio);
        this.fatigueRatio += Math.abs(fatigueRatio);
        this.sinkStatusRatioToStatusValue();

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_STATUS_RATIO)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void decreaseStatusRatio(Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {
        this.strengthRatio -= Math.abs(strengthRatio);
        this.satietyRatio -= Math.abs(satietyRatio);
        this.healthyRatio -= Math.abs(healthyRatio);
        this.fatigueRatio -= Math.abs(fatigueRatio);
        this.sinkStatusRatioToStatusValue();

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_INCREASE_STATUS_RATIO)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void setMaxStatus(Double maxStatus) {

        if (Objects.equals(this.maxStatus, maxStatus)) return;

        this.exp = this.exp / this.maxStatus * maxStatus;
        this.strength = this.strength / this.maxStatus * maxStatus;
        this.satiety = this.satiety / this.maxStatus * maxStatus;
        this.healthy = this.healthy / this.maxStatus * maxStatus;
        this.fatigue = this.fatigue / this.maxStatus * maxStatus;

        this.maxStatus = maxStatus;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_SET_MAX_STATUS)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    public void setCode(MongStatusCode code) {

        if (this.code == code) return;

        this.code = code;

        history.add(MongStatusHistoryEntity.builder()
                .mongStatusHistoryCode(MongStatusHistoryCode.HISTORY_MONG_STATUS_SET_CODE)
                .maxStatus(maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }

    private void sinkStatusRatioToStatusValue() {
        this.strengthRatio = Math.max(0D, Math.min(this.strengthRatio, 100D));
        this.satietyRatio = Math.max(0D, Math.min(this.satietyRatio, 100D));
        this.healthyRatio = Math.max(0D, Math.min(this.healthyRatio, 100D));
        this.fatigueRatio = Math.max(0D, Math.min(this.fatigueRatio, 100D));

        this.strength = this.strengthRatio * this.maxStatus / 100;
        this.satiety = this.satietyRatio * this.maxStatus / 100;
        this.healthy = this.healthyRatio * this.maxStatus / 100;
        this.fatigue = this.fatigueRatio * this.maxStatus / 100;
    }

    private void sinkStatusValueToStatusRatio() {
        this.strength = Math.max(0D, Math.min(this.strength, this.maxStatus));
        this.satiety = Math.max(0D, Math.min(this.satiety, this.maxStatus));
        this.healthy = Math.max(0D, Math.min(this.healthy, this.maxStatus));
        this.fatigue = Math.max(0D, Math.min(this.fatigue, this.maxStatus));

        this.strengthRatio = this.strength / this.maxStatus * 100;
        this.satietyRatio = this.satiety / this.maxStatus * 100;
        this.healthyRatio = this.healthy / this.maxStatus * 100;
        this.fatigueRatio = this.fatigue / this.maxStatus * 100;
    }

    public void sink() {
        this.weight = Math.max(0D, this.weight);
        this.poopCount = Math.max(0, Math.min(this.poopCount, MAX_POOP_COUNT));

        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;
    }
}
