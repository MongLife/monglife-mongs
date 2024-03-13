package com.mongs.auth.client;

public record RegisterMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
}
