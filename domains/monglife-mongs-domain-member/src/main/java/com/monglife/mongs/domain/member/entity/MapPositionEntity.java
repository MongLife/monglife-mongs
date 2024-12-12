package com.monglife.mongs.domain.member.entity;

import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_map_position")
public class MapPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_position_id")
    private Long mapPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_type_code")
    private ComnCodeEntity comn;

    @Column(name = "address")
    private String address;

    @Column(name = "address_road")
    private String addressRoad;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_x")
    private Double positionX;

    @Builder
    public MapPositionEntity(ComnCodeEntity comn, String address, String addressRoad, Double positionY, Double positionX) {
        this.comn = comn;
        this.address = address;
        this.addressRoad = addressRoad;
        this.positionY = positionY;
        this.positionX = positionX;
    }
}
