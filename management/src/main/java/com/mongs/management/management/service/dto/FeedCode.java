package com.mongs.management.management.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record FeedCode (
    String feedCode
){
}
