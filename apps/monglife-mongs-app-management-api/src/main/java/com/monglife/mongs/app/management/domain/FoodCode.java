package com.monglife.mongs.app.management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCode {

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String groupCode;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Double addWeightValue;

    @Column(nullable = false)
    private Double addStrengthValue;

    @Column(nullable = false)
    private Double addSatietyValue;

    @Column(nullable = false)
    private Double addHealthyValue;

    @Column(nullable = false)
    private Double addSleepValue;

    @Column(nullable = false)
    private Integer delaySeconds;
}
