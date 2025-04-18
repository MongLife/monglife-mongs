package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.enums.OrderTypeCode;
import com.monglife.mongs.domain.model.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_order")
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type")
    private ComnCodeEntity productType;

    @Column(name = "price")
    private Double price;

    @Column(name = "social_order_id")
    private String socialOrderId;

    @Column(name = "purchase_token")
    private String purchaseToken;

    @Column(name = "is_consumed")
    private Boolean isConsumed = false;

    @Builder
    public OrderEntity(Long accountId, ComnCodeEntity productType, Double price, String socialOrderId, String purchaseToken) {
        this.accountId = accountId;
        this.productType = productType;
        this.price = price;
        this.socialOrderId = socialOrderId;
        this.purchaseToken = purchaseToken;
    }

    /**
     * 주문 엔티티 수정
     * @param order 주문 도메인 객체
     * @param productType 인앱 상품 정보 공통 코드 엔티티
     */
    public void update(Order order, ComnCodeEntity productType) {
        this.accountId = order.getAccountId();
        this.productType = productType;
        this.price = order.getPrice();
        this.socialOrderId = order.getSocialOrderId();
        this.purchaseToken = order.getPurchaseToken();
        this.isConsumed = order.getOrderTypeCode() == OrderTypeCode.CONSUMED;
    }

    /**
     * 주문 엔티티 도메인 변환
     * @return 주문 도메인 객체
     */
    public Order toDomain() {
        return Order.builder()
                .orderId(this.orderId)
                .accountId(this.accountId)
                .productId(productType.getCode())
                .price(this.price)
                .socialOrderId(this.socialOrderId)
                .purchaseToken(this.purchaseToken)
                .orderTypeCode(this.isConsumed ? OrderTypeCode.CONSUMED : OrderTypeCode.ORDERED)
                .build();
    }
}
