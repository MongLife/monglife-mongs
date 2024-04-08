package com.mongs.management.domain.mong.service.event.vo;

import com.mongs.management.domain.mong.entity.Mong;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record StateCheckEvent(
        Mong mong
) {
}
