package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.model.TrainingType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_training_type")
@ToString
public class TrainingTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_type_id")
    private Long trainingTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_code")
    private ComnCodeEntity trainingType;

    @Column(name = "pay_point")
    private Integer payPoint;

    @Column(name = "score")
    private Integer score;

    @Column(name = "timeout")
    private Integer timeout;

    @Column(name = "exp")
    private Double exp;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "fatigue")
    private Double fatigue;

    @Builder
    public TrainingTypeEntity(Long trainingTypeId, ComnCodeEntity trainingType, Integer payPoint, Integer score, Integer timeout, Double exp, Double weight, Double satiety, Double fatigue) {
        this.trainingTypeId = trainingTypeId;
        this.trainingType = trainingType;
        this.payPoint = payPoint;
        this.score = score;
        this.timeout = timeout;
        this.exp = exp;
        this.weight = weight;
        this.satiety = satiety;
        this.fatigue = fatigue;
    }

    public TrainingType toDomain() {
        return TrainingType.builder()
                .trainingTypeId(this.trainingTypeId)
                .trainingTypeCode(this.trainingType.getCode())
                .trainingTypeName(this.trainingType.getName())
                .payPoint(this.payPoint)
                .score(this.score)
                .timeout(this.timeout)
                .exp(this.exp)
                .weight(this.weight)
                .satiety(this.satiety)
                .fatigue(this.fatigue)
                .build();
    }
}
