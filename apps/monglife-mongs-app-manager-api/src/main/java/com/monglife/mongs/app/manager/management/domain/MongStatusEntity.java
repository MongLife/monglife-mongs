package com.monglife.mongs.app.manager.management.domain;

import com.monglife.mongs.app.manager.management.enums.MongStatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EntityListeners(MongStatusEntityListener.class)
@Table(name = "mongs_manager_mong_status")
//@ToString(exclude = "mong", callSuper = true)
public class MongStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_id")
    private Long mongStatusId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mong_id")
//    private MongEntity mong;

    @Column(name = "max_status")
    private Double maxStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_status_code")
    private MongStatusCode code;

    @Column(name = "exp")
    private Double exp;

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

    @Column(name = "poop_count")
    private Integer poopCount;

    @Column(name = "weight_ratio")
    private Double weightRatio;

    @Column(name = "strength_ratio")
    private Double strengthRatio;

    @Column(name = "satiety_ratio")
    private Double satietyRatio;

    @Column(name = "healthy_ratio")
    private Double healthyRatio;

    @Column(name = "fatigue_ratio")
    private Double fatigueRatio;

    public MongStatusEntity(MongEntity mong, Double maxStatus) {
//        this.mong = mong;

        this.maxStatus = maxStatus;
        this.code = MongStatusCode.NORMAL;
        this.exp = 0D;
        this.weight = maxStatus;
        this.strength = maxStatus;
        this.satiety = maxStatus;
        this.healthy = maxStatus;
        this.fatigue = maxStatus;
        this.poopCount = 0;

        this.weightRatio = 100D;
        this.strengthRatio = 100D;
        this.satietyRatio = 100D;
        this.healthyRatio = 100D;
        this.fatigueRatio = 100D;
    }

    public void updateMaxStatus(Double maxStatus) {

        this.weight = this.weight / this.maxStatus * maxStatus;
        this.strength = this.strength / this.maxStatus * maxStatus;
        this.satiety = this.satiety / this.maxStatus * maxStatus;
        this.healthy = this.healthy / this.maxStatus * maxStatus;
        this.fatigue = this.fatigue / this.maxStatus * maxStatus;

        this.maxStatus = maxStatus;
    }

    public void update(UpdateDto updateDto) {
        this.maxStatus = Optional.ofNullable(updateDto.maxStatus).orElse(this.maxStatus);
        this.code = Optional.ofNullable(updateDto.code).orElse(this.code);
        this.exp = Optional.ofNullable(updateDto.exp).orElse(this.exp);
        this.weight = Optional.ofNullable(updateDto.weight).orElse(this.weight);
        this.strength = Optional.ofNullable(updateDto.strength).orElse(this.strength);
        this.satiety = Optional.ofNullable(updateDto.satiety).orElse(this.satiety);
        this.healthy = Optional.ofNullable(updateDto.healthy).orElse(this.healthy);
        this.fatigue = Optional.ofNullable(updateDto.fatigue).orElse(this.fatigue);
        this.poopCount = Optional.ofNullable(updateDto.poopCount).orElse(this.poopCount);

        this.weight = Math.min(0D, Math.min(this.weight, this.maxStatus));
        this.strength = Math.min(0D, Math.min(this.strength, this.maxStatus));
        this.satiety = Math.min(0D, Math.min(this.satiety, this.maxStatus));
        this.healthy = Math.min(0D, Math.min(this.healthy, this.maxStatus));
        this.fatigue = Math.min(0D, Math.min(this.fatigue, this.maxStatus));

        this.weightRatio = this.weight / this.maxStatus * 100;
        this.strengthRatio = this.strength / this.maxStatus * 100;
        this.satietyRatio = this.satiety / this.maxStatus * 100;
        this.healthyRatio = this.healthy / this.maxStatus * 100;
        this.fatigueRatio = this.fatigue / this.maxStatus * 100;
    }

    @Builder
    @AllArgsConstructor
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
