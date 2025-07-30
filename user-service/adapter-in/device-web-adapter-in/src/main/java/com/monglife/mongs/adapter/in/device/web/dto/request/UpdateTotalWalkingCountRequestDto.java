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
public class UpdateTotalWalkingCountRequestDto {

    @Min(0)
    private Integer totalWalkingCount;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deviceBootedAt;

    @Builder
    public UpdateTotalWalkingCountRequestDto(Integer totalWalkingCount, LocalDateTime deviceBootedAt) {
        this.totalWalkingCount = totalWalkingCount;
        this.deviceBootedAt = deviceBootedAt;
    }
}
