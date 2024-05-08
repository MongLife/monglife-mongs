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
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
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
    private Integer exp = 0;
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

    public void setExp(Integer exp) {
        this.exp = Math.max(0, Math.min(exp, this.grade.nextGrade.evolutionExp));
    }
    public void setWeight(Double weight) {
        this.weight = Math.max(0, Math.min(weight, this.grade.maxStatus));
    }
    public void setStrength(Double strength) {
        this.strength = Math.max(0, Math.min(strength, this.grade.maxStatus));
    }
    public void setSatiety(Double satiety) {
        this.satiety = Math.max(0, Math.min(satiety, this.grade.maxStatus));
    }
    public void setHealthy(Double healthy) {
        this.healthy = Math.max(0, Math.min(healthy, this.grade.maxStatus));
    }
    public void setSleep(Double sleep) {
        this.sleep = Math.max(0, Math.min(sleep, this.grade.maxStatus));
    }
    public void setPoopCount(Integer poopCount) {
        this.poopCount = Math.max(0, Math.min(poopCount, 4));
    }
    /*
    // 증가
    public void addExp(Integer exp) {
        this.exp = Math.min(this.exp + exp, this.grade.getNextGrade().getEvolutionExp());
    }
    public void addWeight(Double weight) {
        this.weight = Math.min(this.weight + weight, this.grade.getMaxStatus());
    }
    public void addStrength(Double strength) {
        this.strength = Math.min(this.strength + strength, this.grade.getMaxStatus());
    }
    public void addSatiety(Double satiety) {
        this.satiety = Math.min(this.satiety + satiety, this.grade.getMaxStatus());
    }
    public void addHealthy(Double healthy) {
        this.healthy = Math.min(this.healthy + healthy, this.grade.getMaxStatus());
    }
    public void addSleep(Double sleep) {
        this.sleep = Math.min(this.sleep + sleep, this.grade.getMaxStatus());
    }
    public void addPoopCount(Integer poopCount) {
        this.poopCount = Math.min(this.poopCount + poopCount, 4);
    }

    // 감소
    public void subWeight(Double weight) {
        this.weight = Math.max(this.weight - weight, 0D);
    }
    public void subStrength(Double strength) {
        this.strength = Math.max(this.strength - strength, 0D);
    }
    public void subSatiety(Double satiety) {
        this.satiety = Math.max(this.satiety - satiety, 0D);
    }
    public void subHealthy(Double healthy) {
        this.healthy = Math.max(this.healthy - healthy, 0D);
    }
    public void subSleep(Double sleep) {
        this.sleep = Math.max(this.sleep - sleep, 0D);
    }
    */
}
