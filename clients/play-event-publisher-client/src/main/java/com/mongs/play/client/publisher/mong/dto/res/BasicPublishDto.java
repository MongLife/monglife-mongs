package com.mongs.play.client.publisher.mong.dto.res;

import com.mongs.play.client.publisher.mong.code.PublishCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BasicPublishDto<T> {
    private PublishCode code;
    private T data;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
