package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.entity.Mong;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TrainingMongResDto(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
}
