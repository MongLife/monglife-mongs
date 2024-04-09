package com.mongs.management.global.event.vo;

import com.mongs.management.global.entity.Mong;

public record StateCheckEvent(
        Mong mong
) {
}
