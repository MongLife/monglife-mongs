package com.mongs.play.client.publisher.event.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MongStateVo{
    private Long mongId;
    private String stateCode;
}
