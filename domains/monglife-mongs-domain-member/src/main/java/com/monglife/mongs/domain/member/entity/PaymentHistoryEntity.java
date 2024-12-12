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
@Table(name = "mongs_payment_history")
public class PaymentHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long paymentHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_history_type")
    private PaymentHistoryType type;

    @Builder
    public PaymentHistoryEntity(PaymentHistoryType type) {
        this.type = type;
    }

    public enum PaymentHistoryType {
        REQUEST,
        REWARD,
        DONE,
    }
}
