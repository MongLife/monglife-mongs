package com.monglife.mongs.adapter.in.device.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountRequestDto {

    @NotNull
    private Long mongId;

    @Min(0)
    private Integer totalWalkingCount;

    @Min(0)
    private Integer walkingCount;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deviceBootedDt;

    @Builder
    public ExchangeCurrentWalkingCountRequestDto(Long mongId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {
        this.mongId = mongId;
        this.totalWalkingCount = totalWalkingCount;
        this.walkingCount = walkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }
}
