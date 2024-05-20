package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MemberStarPointVo(
        Long accountId,
        Integer starPoint
) {
}
