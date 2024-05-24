package com.mongs.play.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class ChargeItem {
    @Id
    @Column(name = "charge_item_id")
    @Builder.Default
    private String chargeItemId = UUID.randomUUID().toString().replace("-", "");
    @Column(updatable = false, nullable = false)
    private String name;
    @Column(updatable = false, nullable = false)
    private Integer price;
    @Column(updatable = false, nullable = false)
    private Integer starPoint;
}
