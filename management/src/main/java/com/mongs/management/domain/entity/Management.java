package com.mongs.management.domain.entity;

import com.mongs.core.time.BaseTimeEntity;
import com.mongs.management.domain.service.dto.TrainingCount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder(toBuilder = true)
public class Management extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String name;

    private double weight;

    @Builder.Default
    private int strength = 100;

    @Builder.Default
    private int satiety = 100;

    @Builder.Default
    private int healthy = 100;

    private Boolean sleep;

    @Builder.Default
    private int penalty = 0;

    @Builder.Default
    private int trainingCount = 0;

    @Builder.Default
    private int strokeCount = 0;

    @Builder.Default
    private int poopCount = 0;

    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;

    @Builder.Default
    private int paypoint = 0;

    public void doStroke(int stroke) {
        this.strokeCount = stroke + 1;
    }

    public void changeSleepConditon (Boolean isSleep) {
        this.sleep = isSleep;
    }

    public void setPoopCount(int poopCount) {
        this.poopCount = poopCount;
    }

    public void setTrainingCount(int trainingCount) {
        this.trainingCount += trainingCount;
    }
}
