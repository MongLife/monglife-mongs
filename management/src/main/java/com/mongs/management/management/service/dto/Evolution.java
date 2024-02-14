package com.mongs.management.management.service.dto;

import lombok.*;

/**
 * mongCode: String
 * stateCode: String
 * weight: Integer
 */


@Builder
public record Evolution (
    String mongCode,
    String stateCode,
    double weight
){
}
