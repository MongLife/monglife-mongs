package com.mongs.management.management.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder(toBuilder = true)
public record SlotListResponse (
    Long mongId,
    String name,
    String mongCode,
    String stateCode,
    int poopCount,
    String mapCode
){
}
