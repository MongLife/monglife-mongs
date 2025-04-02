package com.mongs.play.app.management.external.dto.req;

public record RegisterMongReqDto(
        String name,
        String sleepStart,
        String sleepEnd
) {
}
