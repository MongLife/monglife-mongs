package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongStatusHistoryEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, MongStatusHistoryEntityListener.class})
@Table(name = "mongs_mong_status_history")
public class MongStatusHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_history_id")
    private Long mongStatusHistoryId;

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_name")
    private String mongName;

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
    public MongStatusHistoryEntity(Long mongId, Long accountId, String mongName, MongStatusHistoryType mongStatusHistoryType, Double maxStatus, MongStatusCode code, Double weight, Integer poopCount, Double exp, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {
        this.mongId = mongId;
        this.accountId = accountId;
        this.mongName = mongName;
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

        PATCH_STATUS("지수 갱신"),
        INCREASE_EXP("경험치 증가"),
        DECREASE_EXP("경험치 감소"),
        RESET_EXP("경험치 초기화"),
        INCREASE_POOP_COUNT("배변 수 증가"),
        DECREASE_POOP_COUNT("배변 수 감소"),
        RESET_POOP_COUNT("배변 수 초기화"),
        INCREASE_WEIGHT("몸무게 증가"),
        DECREASE_WEIGHT("몸무게 감소"),
        INCREASE_STATUS("지수 증가"),
        DECREASE_STATUS("지수 감소"),
        INCREASE_STATUS_RATIO("지수 비율 증가"),
        DECREASE_STATUS_RATIO("지수 비율 감소"),
        SET_MAX_STATUS("최대 지수 값 변경"),
        SET_CODE("지수 코드 값 변경")
        ;

        public final String name;
    }
}
