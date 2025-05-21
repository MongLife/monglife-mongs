package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_snack_type")
public class SnackTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snack_type_id")
    private Long snackTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_type_code")
    private ComnCodeEntity comn;

    @Column(name = "price")
    private Integer price;

    @Column(name = "add_weight_value")
    private Double weight;

    @Column(name = "add_strength_value")
    private Double strength;

    @Column(name = "add_satiety_value")
    private Double satiety;

    @Column(name = "add_healthy_value")
    private Double healthy;

    @Column(name = "add_fatigue_value")
    private Double fatigue;

    @Column(name = "delay_seconds")
    private Integer delaySeconds;

    @Builder
    public SnackTypeEntity(Long snackTypeId, ComnCodeEntity comn, Integer price, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer delaySeconds) {
        this.snackTypeId = snackTypeId;
        this.comn = comn;
        this.price = price;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.delaySeconds = delaySeconds;
    }
}
