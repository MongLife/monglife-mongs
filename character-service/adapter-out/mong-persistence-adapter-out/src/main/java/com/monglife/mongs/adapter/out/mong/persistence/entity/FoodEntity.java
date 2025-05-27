package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_food")
@ToString
public class FoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_code")
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
    public FoodEntity(Long foodId, ComnCodeEntity comn, Integer price, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer delaySeconds) {
        this.foodId = foodId;
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
