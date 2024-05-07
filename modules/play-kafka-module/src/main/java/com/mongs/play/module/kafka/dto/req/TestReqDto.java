package com.mongs.play.module.kafka.dto.req;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TestReqDto {
    private String message;
    private Integer count;
    private LocalDateTime createdAt;
}
