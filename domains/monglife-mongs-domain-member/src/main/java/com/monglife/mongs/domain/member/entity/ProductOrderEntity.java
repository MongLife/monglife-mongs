package com.monglife.mongs.domain.member.entity;


import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_product_order")
public class ProductOrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id")
    private Long productOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_code")
    private ComnCodeEntity comn;

    @Column(name = "price")
    private Double price;

    @Column(name = "receipt")
    private String receipt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_order_id")
    private List<ProductOrderHistoryEntity> history;

    @Builder
    public ProductOrderEntity(MemberEntity member, ComnCodeEntity comn, Double price) {
        this.member = member;
        this.comn = comn;
        this.price = price;
        this.history = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {

        ProductOrderHistoryEntity productOrderHistoryEntity = ProductOrderHistoryEntity.builder()
                .type(ProductOrderHistoryEntity.ProductOrderHistoryType.ORDER)
                .build();

        this.history.add(productOrderHistoryEntity);
    }

    public void consume(String receipt) {

        this.receipt = receipt;

        ProductOrderHistoryEntity productOrderHistoryEntity = ProductOrderHistoryEntity.builder()
                .type(ProductOrderHistoryEntity.ProductOrderHistoryType.CONSUME)
                .build();

        this.history.add(productOrderHistoryEntity);
    }

    public void done() {

        ProductOrderHistoryEntity productOrderHistoryEntity = ProductOrderHistoryEntity.builder()
                .type(ProductOrderHistoryEntity.ProductOrderHistoryType.DONE)
                .build();

        this.history.add(productOrderHistoryEntity);
    }
}
