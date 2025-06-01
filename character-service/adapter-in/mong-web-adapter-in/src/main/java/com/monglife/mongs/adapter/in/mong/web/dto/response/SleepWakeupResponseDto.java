package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SleepWakeupResponseDto {

    private Long mongId;

    private Boolean isSleep;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public SleepWakeupResponseDto(Long mongId, Boolean isSleep, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.isSleep = isSleep;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
