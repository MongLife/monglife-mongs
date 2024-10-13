package com.monglife.mongs.app.management.presentation.dto.req;

public record RegisterMongReqDto(
        String name,
        String sleepStart,
        String sleepEnd
) {
}
