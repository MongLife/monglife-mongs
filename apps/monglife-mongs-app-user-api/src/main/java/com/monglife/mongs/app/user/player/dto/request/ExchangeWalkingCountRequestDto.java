package com.monglife.mongs.app.user.player.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeWalkingCountRequestDto {

    private Long mongId;

    @Min(1)
    private Integer walkingCount;

    @Builder
    public ExchangeWalkingCountRequestDto(Long mongId, Integer walkingCount) {
        this.mongId = mongId;
        this.walkingCount = walkingCount;
    }
}
