package com.mongs.management.domain.mongEvent.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
public record EventOccurrence (
    Long mongId,
    String event
){
}
