package com.mongs.management.management.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SlotListResponse {
    private Long mongId;
    private String name;
    private String mongCode;
    private String stateCode;
    private int poopCount;
    private String mapCode;
}
