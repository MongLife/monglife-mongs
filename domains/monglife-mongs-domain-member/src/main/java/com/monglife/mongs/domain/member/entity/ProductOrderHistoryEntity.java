package com.monglife.mongs.domain.member.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_product_order_history")
public class ProductOrderHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_history_id")
    private Long productOrderHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_order_history_type")
    private ProductOrderHistoryType type;

    @Builder
    public ProductOrderHistoryEntity(ProductOrderHistoryType type) {
        this.type = type;
    }

    public enum ProductOrderHistoryType {
        ORDER,
        CONSUME,
    }
}
