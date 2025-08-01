package com.monglife.mongs.adapter.out.mong.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_mong_group_type")
public class MongGroupTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_group_type_id")
    private Long mongGroupTypeId;

    @Column(name = "group_type")
    private String groupType;

    @Column(name = "next_group_type")
    private String nextGroupType;

    @Builder
    public MongGroupTypeEntity(Long mongGroupTypeId, String groupType, String nextGroupType) {
        this.mongGroupTypeId = mongGroupTypeId;
        this.groupType = groupType;
        this.nextGroupType = nextGroupType;
    }
}
