package com.mongs.play.domain.account.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Table(name = "account_log")
public class AccountLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_log_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(updatable = false, nullable = false)
    private String deviceId;
    @Column(updatable = false, nullable = false)
    private LocalDate loginAt;
    @Column(nullable = false)
    @Builder.Default
    private Integer loginCount = 0;
}
