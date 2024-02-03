package com.mongs.management.management.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long id;

    private String name;

    private LocalDateTime regDt;

    private double weight;

    private int age;

    private int strength;

    private int satiety;

    private int healthy;

    private int sleep;

    private int penalty;

    private int trainingCount;

    private int strokeCount;

    private int poopCount;

    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;

    private int paypoint;

    @Builder
    public Management(Long id, String name, LocalDateTime regDt, double weight, int age, int strength, int satiety, int healthy, int sleep, int penalty, int trainingCount, int strokeCount, int poopCount, LocalDateTime sleepStart, LocalDateTime sleepEnd, int paypoint) {
        this.id = id;
        this.name = name;
        this.regDt = regDt;
        this.weight = weight;
        this.age = age;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.sleep = sleep;
        this.penalty = penalty;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.poopCount = poopCount;
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.paypoint = paypoint;
    }

    @Override
    public String toString() {
        return "Management{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", regDt=" + regDt +
                ", weight=" + weight +
                ", age=" + age +
                ", strength=" + strength +
                ", satiety=" + satiety +
                ", healthy=" + healthy +
                ", sleep=" + sleep +
                ", penalty=" + penalty +
                ", trainingCount=" + trainingCount +
                ", strokeCount=" + strokeCount +
                ", poopCount=" + poopCount +
                ", sleepStart=" + sleepStart +
                ", sleepEnd=" + sleepEnd +
                ", paypoint=" + paypoint +
                '}';
    }
}
