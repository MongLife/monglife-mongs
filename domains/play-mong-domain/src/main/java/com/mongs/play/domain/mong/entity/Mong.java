package com.mongs.play.domain.mong.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
public class Mong extends BaseTimeEntity {

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String sleepTime;
    @Column(nullable = false)
    private String wakeUpTime;
    @Builder.Default
    @Column(nullable = false)
    private String mongCode = "CD444";
    @Builder.Default
    @Column(nullable = false)
    private Integer payPoint = 0;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongGrade grade = MongGrade.EMPTY;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongShift shift = MongShift.EMPTY;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongState state = MongState.EMPTY;

    @Builder.Default
    @Column(nullable = false)
    private Double exp = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double weight = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double strength = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double satiety = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double healthy = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double sleep = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Integer poopCount = 0;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isSleeping = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer evolutionPoint = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer penalty = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer numberOfTraining = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer numberOfStroke = 0;

    public Mong validation() {
        this.exp = Math.max(0, Math.min(this.exp, this.grade.evolutionExp));
        this.weight = Math.max(0, Math.min(this.weight, this.grade.maxStatus));
        this.strength = Math.max(0, Math.min(this.strength, this.grade.maxStatus));
        this.satiety = Math.max(0, Math.min(this.satiety, this.grade.maxStatus));
        this.healthy = Math.max(0, Math.min(this.healthy, this.grade.maxStatus));
        this.sleep = Math.max(0, Math.min(this.sleep, this.grade.maxStatus));
        this.poopCount = Math.max(0, Math.min(this.poopCount, 4));
        return this;
    }
}
