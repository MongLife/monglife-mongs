package com.mongs.play.client.publisher.mong.dto.res;

import com.mongs.play.client.publisher.mong.code.PublishCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicPublishDto<T> {
    private PublishCode code;
    private T data;
}
