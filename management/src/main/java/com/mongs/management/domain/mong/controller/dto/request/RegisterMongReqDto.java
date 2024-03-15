package com.mongs.management.domain.mong.controller.dto.request;

public record RegisterMongReqDto(
        String name,
        String sleepStart,
        String sleepEnd
) {
}
