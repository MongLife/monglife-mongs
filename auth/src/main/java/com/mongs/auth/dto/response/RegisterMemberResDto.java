package com.mongs.auth.dto.response;

public record RegisterMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
}
