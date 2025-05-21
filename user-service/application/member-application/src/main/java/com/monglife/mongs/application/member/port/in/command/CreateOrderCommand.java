package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderCommand {

    private final Long accountId;

    private final String productId;

    private final String socialOrderId;

    private final String purchaseToken;

    @Builder
    public CreateOrderCommand(Long accountId, String productId, String socialOrderId, String purchaseToken) {
        this.accountId = accountId;
        this.productId = productId;
        this.socialOrderId = socialOrderId;
        this.purchaseToken = purchaseToken;
    }
}
