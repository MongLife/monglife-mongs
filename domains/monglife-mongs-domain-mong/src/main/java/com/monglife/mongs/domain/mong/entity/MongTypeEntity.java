package com.monglife.mongs.domain.mong.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @JoinColumn(name = "mong_type_code")
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
}