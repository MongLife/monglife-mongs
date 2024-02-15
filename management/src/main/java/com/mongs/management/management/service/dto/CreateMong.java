package com.mongs.management.management.service.dto;


import com.mongs.common.ManagementStateCode;
import com.mongs.management.management.entity.Management;
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
                .mongCode(ManagementStateCode.SUCCESS.getCode())
                .weight(management.getWeight())
                .build();
    }

}
