package com.mongs.management.domain.management.controller.dto.request;

public record RegisterMongReqDto(
        String name,
        String sleepStart,
        String sleepEnd
) {
}
