package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.domain.mong.entity.Mong;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record CreateMong (
    Long id,
    String name,
    String mongCode,
    double weight,
    LocalDateTime bornTime
){
    public static CreateMong of(Mong mong) {
        return CreateMong.builder()
                .id(mong.getId())
                .name(mong.getName())
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .weight(mong.getWeight())
                .build();
    }

}
