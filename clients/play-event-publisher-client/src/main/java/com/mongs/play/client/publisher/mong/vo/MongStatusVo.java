package com.mongs.play.client.publisher.mong.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MongStatusVo{
    private Long mongId;
    private Double weight;
    private Double strength;
    private Double satiety;
    private Double healthy;
    private Double sleep;
}
