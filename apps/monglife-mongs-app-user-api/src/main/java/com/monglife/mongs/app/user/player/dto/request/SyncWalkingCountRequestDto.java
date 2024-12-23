package com.monglife.mongs.app.user.player.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SyncWalkingCountRequestDto {

    @NotBlank
    private String deviceId;

    @Min(0)
    private Integer totalWalkingCount;

    @NotBlank
    private LocalDateTime deviceBootedDt;

    @Builder
    public SyncWalkingCountRequestDto(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.totalWalkingCount = totalWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }
}
