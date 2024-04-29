package com.mongs.lifecycle.service.event.vo;

import com.mongs.lifecycle.entity.Mong;

public record StateCheckEvent(
        Mong mong
) {
}
