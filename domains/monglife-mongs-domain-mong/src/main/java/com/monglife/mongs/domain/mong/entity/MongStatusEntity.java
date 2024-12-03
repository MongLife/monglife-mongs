package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongStatusEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(MongStatusEntityListener.class)
@Table(name = "mongs_mong_status")
@ToString(exclude = "mong")
public class MongStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_id")
    private Long mongStatusId;

    @OneToOne(mappedBy = "status", fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_status_id")
    private MongEntity mong;

    @Column(name = "max_status")
    private Double maxStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_status_code")
    private MongStatusCode code;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "poop_count")
    private Integer poopCount;

    @Column(name = "exp")
    private Double exp;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "healthy")
    private Double healthy;

    @Column(name = "fatigue")
    private Double fatigue;

    @Column(name = "exp_ratio")
    private Double expRatio;

    @Column(name = "strength_ratio")
    private Double strengthRatio;

    @Column(name = "satiety_ratio")
    private Double satietyRatio;

    @Column(name = "healthy_ratio")
    private Double healthyRatio;

    @Column(name = "fatigue_ratio")
    private Double fatigueRatio;

    public MongStatusEntity(Double maxStatus) {

        this.maxStatus = maxStatus;
        this.code = MongStatusCode.NORMAL;
        this.weight = 0D;
        this.poopCount = 0;

        this.exp = 0D;
        this.strength = maxStatus;
        this.satiety = maxStatus;
        this.healthy = maxStatus;
        this.fatigue = maxStatus;

        this.expRatio = 0D;
        this.strengthRatio = 100D;
        this.satietyRatio = 100D;
        this.healthyRatio = 100D;
        this.fatigueRatio = 100D;
    }

    public void updateMaxStatus(Double maxStatus) {

        this.exp = this.exp / this.maxStatus * maxStatus;
        this.strength = this.strength / this.maxStatus * maxStatus;
        this.satiety = this.satiety / this.maxStatus * maxStatus;
        this.healthy = this.healthy / this.maxStatus * maxStatus;
        this.fatigue = this.fatigue / this.maxStatus * maxStatus;

        this.maxStatus = maxStatus;
    }

    public void update(UpdateDto updateDto) {
        this.maxStatus = Optional.ofNullable(updateDto.maxStatus).orElse(this.maxStatus);
        this.code = Optional.ofNullable(updateDto.code).orElse(this.code);

        this.weight = Optional.ofNullable(updateDto.weight).orElse(this.weight);
        this.poopCount = Optional.ofNullable(updateDto.poopCount).orElse(this.poopCount);
        this.exp = Optional.ofNullable(updateDto.exp).orElse(this.exp);
        this.strength = Optional.ofNullable(updateDto.strength).orElse(this.strength);
        this.satiety = Optional.ofNullable(updateDto.satiety).orElse(this.satiety);
        this.healthy = Optional.ofNullable(updateDto.healthy).orElse(this.healthy);
        this.fatigue = Optional.ofNullable(updateDto.fatigue).orElse(this.fatigue);

        this.weight = Math.max(0D, this.weight);
        this.poopCount = Math.max(0, Math.min(this.poopCount, 4));
        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.strength = Math.max(0D, Math.min(this.strength, this.maxStatus));
        this.satiety = Math.max(0D, Math.min(this.satiety, this.maxStatus));
        this.healthy = Math.max(0D, Math.min(this.healthy, this.maxStatus));
        this.fatigue = Math.max(0D, Math.min(this.fatigue, this.maxStatus));

        this.expRatio = this.exp / this.maxStatus * 100;
        this.strengthRatio = this.strength / this.maxStatus * 100;
        this.satietyRatio = this.satiety / this.maxStatus * 100;
        this.healthyRatio = this.healthy / this.maxStatus * 100;
        this.fatigueRatio = this.fatigue / this.maxStatus * 100;
    }

    @Builder
    @AllArgsConstructor
    @ToString
    public static class UpdateDto {

        private Double maxStatus;

        private MongStatusCode code;

        private Double exp;

        private Double weight;

        private Double strength;

        private Double satiety;

        private Double healthy;

        private Double fatigue;

        private Integer poopCount;
    }
}
