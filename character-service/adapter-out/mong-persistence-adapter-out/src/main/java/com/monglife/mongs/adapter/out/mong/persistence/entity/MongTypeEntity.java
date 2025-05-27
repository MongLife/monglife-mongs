package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.model.MongType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_mong_type")
@ToString
public class MongTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_type_id")
    private Long mongTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_code")
    private ComnCodeEntity comn;

    @Column(name = "level")
    private Integer level;

    @Column(name = "evolution_score")
    private Double evolutionScore;

    @Column(name = "max_status")
    private Double maxStatus;

    @Column(name = "next_group_type")
    private String nextGroupType;

    @Column(name = "group_type")
    private String groupType;

    @Builder
    public MongTypeEntity(Long mongTypeId, ComnCodeEntity comn, Integer level, Double evolutionScore, Double maxStatus, String nextGroupType, String groupType) {
        this.mongTypeId = mongTypeId;
        this.comn = comn;
        this.level = level;
        this.evolutionScore = evolutionScore;
        this.maxStatus = maxStatus;
        this.nextGroupType = nextGroupType;
        this.groupType = groupType;
    }

    /**
     * 엔티티 도메인 변환
     * @return 몽 타입 도메인 객체
     */
    public MongType toDomain() {
        return MongType.builder()
                .mongTypeId(this.mongTypeId)
                .mongCode(this.comn.getCode())
                .mongName(this.comn.getName())
                .level(this.level)
                .evolutionScore(this.evolutionScore)
                .maxStatus(this.maxStatus)
                .build();
    }
}