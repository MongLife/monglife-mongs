package com.mongs.management.domain.service.dto;


import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.domain.entity.Management;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record CreateMong (
    String name,
    String mongCode,
    double weight,
    LocalDateTime borntime
){
    public static CreateMong of(Management management) {
        return CreateMong.builder()
                .name(management.getName())
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .weight(management.getWeight())
                .build();
    }

}
