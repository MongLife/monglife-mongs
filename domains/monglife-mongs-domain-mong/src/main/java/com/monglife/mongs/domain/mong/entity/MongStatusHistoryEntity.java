package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_mong_status_history")
public class MongStatusHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_history_id")
    private Long mongStatusHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_status_history_type")
    private MongStatusHistoryType mongStatusHistoryType;

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

    @Builder
    public MongStatusHistoryEntity(MongStatusHistoryType mongStatusHistoryType, Double maxStatus, MongStatusCode code, Double weight, Integer poopCount, Double exp, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {
        this.mongStatusHistoryType = mongStatusHistoryType;
        this.maxStatus = maxStatus;
        this.code = code;
        this.weight = weight;
        this.poopCount = poopCount;
        this.exp = exp;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
    }

    @Getter
    @AllArgsConstructor
    public enum MongStatusHistoryType {

        HISTORY_MONG_STATUS_INCREASE_EXP("경험치 증가"),
        HISTORY_MONG_STATUS_DECREASE_EXP("경험치 감소"),
        HISTORY_MONG_STATUS_RESET_EXP("경험치 초기화"),
        HISTORY_MONG_STATUS_INCREASE_POOP_COUNT("배변 수 증가"),
        HISTORY_MONG_STATUS_DECREASE_POOP_COUNT("배변 수 감소"),
        HISTORY_MONG_STATUS_RESET_POOP_COUNT("배변 수 초기화"),
        HISTORY_MONG_STATUS_INCREASE_WEIGHT("몸무게 증가"),
        HISTORY_MONG_STATUS_DECREASE_WEIGHT("몸무게 감소"),
        HISTORY_MONG_STATUS_INCREASE_STATUS("지수 증가"),
        HISTORY_MONG_STATUS_DECREASE_STATUS("지수 감소"),
        HISTORY_MONG_STATUS_INCREASE_STATUS_RATIO("지수 비율 증가"),
        HISTORY_MONG_STATUS_DECREASE_STATUS_RATIO("지수 비율 감소"),
        HISTORY_MONG_STATUS_SET_MAX_STATUS("최대 지수 값 변경"),
        HISTORY_MONG_STATUS_SET_CODE("지수 코드 값 변경")
        ;

        public final String name;
    }
}
