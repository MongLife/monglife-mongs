package com.monglife.mongs.adapter.in.device.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountRequestDto {

    @NotNull
    private Long mongId;

    @NotNull
    @Min(1)
    private Integer walkingCount;

    @Builder
    public ExchangeCurrentWalkingCountRequestDto(Long mongId, Integer walkingCount) {
        this.mongId = mongId;
        this.walkingCount = walkingCount;
    }
}
