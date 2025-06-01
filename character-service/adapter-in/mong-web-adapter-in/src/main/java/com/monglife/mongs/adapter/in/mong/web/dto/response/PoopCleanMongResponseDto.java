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
public class PoopCleanMongResponseDto {

    private Long mongId;

    private Double expRatio;

    private Integer poopCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public PoopCleanMongResponseDto(Long mongId, Double expRatio, Integer poopCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.expRatio = expRatio;
        this.poopCount = poopCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
