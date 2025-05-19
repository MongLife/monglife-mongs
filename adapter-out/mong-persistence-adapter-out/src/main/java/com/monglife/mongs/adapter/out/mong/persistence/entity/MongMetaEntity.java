package com.monglife.mongs.adapter.out.mong.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_mong_meta")
@ToString(exclude = { "mong" })
public class MongMetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_meta_id")
    private Long mongMetaId;

    @OneToOne(mappedBy = "meta", cascade = CascadeType.PERSIST)
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

    @Builder
    public MongMetaEntity(Long mongMetaId, MongEntity mong, Integer trainingCount, Integer strokeCount, Double reward, Double penalty, Boolean isActive) {
        this.mongMetaId = mongMetaId;
        this.mong = mong;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.reward = reward;
        this.penalty = penalty;
        this.isActive = isActive;
    }
}
