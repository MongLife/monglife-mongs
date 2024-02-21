package com.mongs.management.service.dto;

import lombok.Builder;


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
