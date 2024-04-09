package com.mongs.management.domain.feed.event.vo;

import com.mongs.management.domain.feed.service.vo.FeedMongVo;

public record FeedMongEvent(
        FeedMongVo feedMongVo
) {
}
