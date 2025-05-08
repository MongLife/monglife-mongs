package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.mongs.domain.model.ExchangeStarPointProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_exchange_star_point_product")
public class ExchangeStarPointProductEntity {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "star_point")
    private Integer starPoint;

    @Builder
    public ExchangeStarPointProductEntity(String productId, String productName, Integer starPoint) {
        this.productId = productId;
        this.productName = productName;
        this.starPoint = starPoint;
    }

    public ExchangeStarPointProduct toDomain() {
        return ExchangeStarPointProduct.builder()
                .productId(this.productId)
                .starPoint(this.starPoint)
                .build();
    }
}
