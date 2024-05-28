package com.mongs.play.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "exchange_item")
public class ExchangeItem {
    @Id
    @Column(name = "exchange_item_id")
    @Builder.Default
    private String exchangeItemId = UUID.randomUUID().toString().replace("-", "");
    @Column(updatable = false, nullable = false)
    private String name;
    @Column(updatable = false, nullable = false)
    private Integer starPoint;
    @Column(updatable = false, nullable = false)
    private Integer payPoint;
}
