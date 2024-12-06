package com.monglife.mongs.domain.mong.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Entity
@Getter
@Table(name = "mongs_mong_meta")
@ToString(exclude = "mong")
public class MongMetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_meta_id")
    private Long mongMetaId;

    @OneToOne(mappedBy = "meta")
    @JoinColumn(name = "mong_meta_id")
    private MongEntity mong;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Column(name = "reward")
    private Double reward;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "is_active")
    private Boolean isActive;


    public MongMetaEntity() {
        this.trainingCount = 0;
        this.strokeCount = 0;
        this.reward = 0D;
        this.penalty = 0D;
        this.isActive = Boolean.TRUE;
    }

    public void update(UpdateDto updateDto) {
        this.trainingCount = Optional.ofNullable(updateDto.trainingCount).orElse(this.trainingCount);
        this.strokeCount = Optional.ofNullable(updateDto.strokeCount).orElse(this.strokeCount);
        this.reward = Optional.ofNullable(updateDto.reward).orElse(this.reward);
        this.penalty = Optional.ofNullable(updateDto.penalty).orElse(this.penalty);
        this.isActive = Optional.ofNullable(updateDto.isActive).orElse(this.isActive);
    }

    @Builder
    @AllArgsConstructor
    public static class UpdateDto {

        private Integer trainingCount;

        private Integer strokeCount;

        private Double reward;

        private Double penalty;

        private Boolean isActive;
    }
}
