package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.exception.ManagementErrorCode;
import lombok.*;

/**
 * mongCode: String
 * stateCode: String
 * weight: Integer
 */


@Builder
public record Evolution (
    String mongCode
){
    public static Evolution of(Mong mong){
        return Evolution.builder()
                .mongCode(mong.getMongCode())
                .build();
    }
}
