package com.monglife.mongs.app.manager.management.domain;

import com.monglife.mongs.module.jpa.domain.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_manager_map_type")
public class MapTypeEntity {

    // https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do (일반음식점)
    // https://data.seoul.go.kr/dataList/OA-16095/S/1/datasetView.do (휴게음식점)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_type_id")
    private Long mapTypeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_type_code")
    private ComnCodeEntity mapCode;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "map_type_id")
    private List<MapPositionEntity> mapPositions;

    @Builder
    public MapTypeEntity(ComnCodeEntity mapCode, List<MapPositionEntity> mapPositions) {
        this.mapCode = mapCode;
        this.mapPositions = mapPositions;
    }
}
