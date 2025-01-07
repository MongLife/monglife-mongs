package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_mong_type")
public class MongTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_type_id")
    private Long mongTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_type_code")
    private ComnCodeEntity comn;

    @Column(name = "level")
    private Integer level;

    @Column(name = "evolution_score")
    private Double evolutionScore;

    @Column(name = "max_status")
    private Double maxStatus;

    @Column(name = "next_type_group_code")
    private String nextTypeGroupCode;
}