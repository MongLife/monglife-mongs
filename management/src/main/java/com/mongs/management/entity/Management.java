package com.mongs.management.entity;

import com.mongs.core.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Management extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String name;

    private LocalDateTime regDt;

    private double weight;

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


}
