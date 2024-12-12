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
@Table(name = "mongs_payment")
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_code")
    private ComnCodeEntity comn;

    @Column(name = "price")
    private Integer price;

    @Column(name = "receipt")
    private String receipt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private List<PaymentHistoryEntity> history;

    @Builder
    public PaymentEntity(Long accountId, ComnCodeEntity comn, Integer price, String receipt) {
        this.accountId = accountId;
        this.comn = comn;
        this.price = price;
        this.receipt = receipt;
        this.history = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {

        PaymentHistoryEntity paymentHistoryEntity = PaymentHistoryEntity.builder()
                .type(PaymentHistoryEntity.PaymentHistoryType.REQUEST)
                .build();

        this.history.add(paymentHistoryEntity);
    }

    public void reward() {

        PaymentHistoryEntity paymentHistoryEntity = PaymentHistoryEntity.builder()
                .type(PaymentHistoryEntity.PaymentHistoryType.REWARD)
                .build();

        this.history.add(paymentHistoryEntity);
    }

    public void done() {

        PaymentHistoryEntity paymentHistoryEntity = PaymentHistoryEntity.builder()
                .type(PaymentHistoryEntity.PaymentHistoryType.DONE)
                .build();

        this.history.add(paymentHistoryEntity);
    }
}
