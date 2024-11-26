package com.monglife.mongs.app.activity.battle.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_activity_mong")
public class MongEntity {

    @Id
    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "mong_name")
    private String mongName;

    @Column(name = "mong_type_code")
    private String mongTypeCode;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "weight_ratio")
    private Double weightRatio;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "strength_ratio")
    private Double strengthRatio;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "satiety_ratio")
    private Double satietyRatio;

    @Column(name = "healthy")
    private Double healthy;

    @Column(name = "healthy_ratio")
    private Double healthyRatio;

    @Column(name = "fatigue")
    private Double fatigue;

    @Column(name = "fatigue_ratio")
    private Double fatigueRatio;
}
