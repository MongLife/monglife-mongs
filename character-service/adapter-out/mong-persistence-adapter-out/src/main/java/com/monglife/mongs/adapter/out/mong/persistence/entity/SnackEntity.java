package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_snack")
@ToString
public class SnackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snack_id")
    private Long snackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_code")
    private ComnCodeEntity comn;

    @Column(name = "price")
    private Integer price;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "healthy")
    private Double healthy;

    @Column(name = "fatigue")
    private Double fatigue;

    @Column(name = "delay_seconds")
    private Integer delaySeconds;

    @Builder
    public SnackEntity(Long snackId, ComnCodeEntity comn, Integer price, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer delaySeconds) {
        this.snackId = snackId;
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
