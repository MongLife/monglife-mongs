package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.domain.mong.entity.Mong;
import lombok.*;

@Builder
public record Graduation(
        String mongCode
)
{
    public static Graduation of (Mong mong) {
        return Graduation.builder()
                .mongCode(mong.getGrade().getCode())
                .build();
    }
}
