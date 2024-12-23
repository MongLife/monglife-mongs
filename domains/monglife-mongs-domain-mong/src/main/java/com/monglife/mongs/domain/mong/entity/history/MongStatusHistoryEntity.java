package com.monglife.mongs.domain.mong.entity.history;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.enums.MongStatusHistoryCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(name = "mong_status_history_code")
    private MongStatusHistoryCode mongStatusHistoryCode;

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
    public MongStatusHistoryEntity(MongStatusHistoryCode mongStatusHistoryCode, Double maxStatus, MongStatusCode code, Double weight, Integer poopCount, Double exp, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {
        this.mongStatusHistoryCode = mongStatusHistoryCode;
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
}
