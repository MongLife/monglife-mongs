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
@Table(name = "mongs_food_type")
public class FoodTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_type_id")
    private Long foodTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_type_code")
    private ComnCodeEntity comn;

    @Column(name = "price")
    private Integer price;

    @Column(name = "add_weight_value")
    private Double addWeightValue;

    @Column(name = "add_strength_value")
    private Double addStrengthValue;

    @Column(name = "add_satiety_value")
    private Double addSatietyValue;

    @Column(name = "add_healthy_value")
    private Double addHealthyValue;

    @Column(name = "add_fatigue_value")
    private Double addFatigueValue;

    @Column(name = "delay_seconds")
    private Integer delaySeconds;

    @Builder
    public FoodTypeEntity(Long foodTypeId, ComnCodeEntity comn, Integer price, Double addWeightValue, Double addStrengthValue, Double addSatietyValue, Double addHealthyValue, Double addFatigueValue, Integer delaySeconds) {
        this.foodTypeId = foodTypeId;
        this.comn = comn;
        this.price = price;
        this.addWeightValue = addWeightValue;
        this.addStrengthValue = addStrengthValue;
        this.addSatietyValue = addSatietyValue;
        this.addHealthyValue = addHealthyValue;
        this.addFatigueValue = addFatigueValue;
        this.delaySeconds = delaySeconds;
    }
}
