package com.mongs.play.client.publisher.mong.vo;

import com.mongs.play.client.publisher.mong.code.PublishCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicPublishVo<T> {
    private PublishCode code;
    private T data;
}
