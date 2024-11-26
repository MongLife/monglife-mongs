package com.monglife.mongs.app.manager.management.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_manager_map_position")
public class MapPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_position_id")
    private Long mapPositionId;

    @Column(name = "map_type_id")
    private Long mapTypeId;

    @Column(name = "address")
    private String address;

    @Column(name = "address_road")
    private String addressRoad;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_x")
    private Double positionX;

    @Builder
    public MapPositionEntity(Long mapTypeId, String address, String addressRoad, Double positionY, Double positionX) {
        this.mapTypeId = mapTypeId;
        this.address = address;
        this.addressRoad = addressRoad;
        this.positionY = positionY;
        this.positionX = positionX;
    }
}
