package com.mongs.play.domain.payment.entity;

import com.mongs.play.domain.core.jpa.baseEntity.BaseTimeEntity;
import com.mongs.play.domain.payment.enums.PaymentCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
public class PaymentLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_log_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(updatable = false, nullable = false)
    private String deviceId;
    @Column(updatable = false)
    private String receipt;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isReward = false;
    @Column(updatable = false, nullable = false)
    private PaymentCode code;
}
