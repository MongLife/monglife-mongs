package com.monglife.mongs.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_map_type")
public class MapTypeEntity {

    // https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do (일반음식점)
    // https://data.seoul.go.kr/dataList/OA-16095/S/1/datasetView.do (휴게음식점)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_type_id")
    private Long mapTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_type_code")
    private ComnCodeEntity mapCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_type_id")
    private List<MapPositionEntity> mapPositions;

    @Builder
    public MapTypeEntity(ComnCodeEntity mapCode, List<MapPositionEntity> mapPositions) {
        this.mapCode = mapCode;
        this.mapPositions = mapPositions;
    }
}
