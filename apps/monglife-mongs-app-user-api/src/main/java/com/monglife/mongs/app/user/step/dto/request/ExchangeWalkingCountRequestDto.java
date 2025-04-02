package com.monglife.mongs.app.user.step.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeWalkingCountRequestDto {

    @NotNull
    private Long mongId;

    @Min(0)
    private Integer totalWalkingCount;

    @Min(0)
    private Integer walkingCount;

    @NotBlank
    private LocalDateTime deviceBootedDt;

    @Builder
    public ExchangeWalkingCountRequestDto(Long mongId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {
        this.mongId = mongId;
        this.totalWalkingCount = totalWalkingCount;
        this.walkingCount = walkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }
}
