package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_map_type")
public class MapTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_type_id")
    private Long mapTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_code")
    private ComnCodeEntity comn;

    @Column(name = "words")
    private String words;

    @Builder
    public MapTypeEntity(Long mapTypeId, ComnCodeEntity comn, String words) {
        this.mapTypeId = mapTypeId;
        this.comn = comn;
        this.words = words;
    }
}
