package com.mongs.management.domain.mong.service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record LastFoodBuyDate (
        LocalDateTime lastBuy
) {
}
