package com.mongs.management.domain.mong.service.dto;


import com.mongs.core.code.MongCode;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.domain.mong.entity.Mong;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record CreateMong (
    String name,
    String mongCode,
    double weight,
    LocalDateTime borntime
){
    public static CreateMong of(Mong mong) {
        return CreateMong.builder()
                .name(mong.getName())
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .weight(mong.getWeight())
                .build();
    }

}
