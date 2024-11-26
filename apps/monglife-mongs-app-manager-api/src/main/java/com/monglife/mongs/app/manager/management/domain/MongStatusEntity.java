package com.monglife.mongs.app.manager.management.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(MongStatusEntityListener.class)
@Table(name = "mongs_manager_mong_status")
@ToString
public class MongStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_id")
    private Long mongStatusId;

    @Column(name = "exp")
    protected Double exp = 0D;

    @Column(name = "weight")
    protected Double weight = 0D;

    @Column(name = "weight_ratio")
    protected Double weightRatio = 0D;

    @Column(name = "strength")
    protected Double strength;

    @Column(name = "strength_ratio")
    protected Double strengthRatio;

    @Column(name = "satiety")
    protected Double satiety;

    @Column(name = "satiety_ratio")
    protected Double satietyRatio;

    @Column(name = "healthy")
    protected Double healthy;

    @Column(name = "healthy_ratio")
    protected Double healthyRatio;

    @Column(name = "fatigue")
    protected Double fatigue;

    @Column(name = "fatigue_ratio")
    protected Double fatigueRatio;

    @Column(name = "poop_count")
    protected Integer poopCount = 0;

    @Builder
    public MongStatusEntity(Double strength, Double strengthRatio, Double satiety, Double satietyRatio, Double healthy, Double healthyRatio, Double fatigue, Double fatigueRatio) {
        this.strength = strength;
        this.strengthRatio = strengthRatio;
        this.satiety = satiety;
        this.satietyRatio = satietyRatio;
        this.healthy = healthy;
        this.healthyRatio = healthyRatio;
        this.fatigue = fatigue;
        this.fatigueRatio = fatigueRatio;
    }
}
