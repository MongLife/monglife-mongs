package com.mongs.auth.client.dto.response;

public record RegisterMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
}
