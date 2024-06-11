package com.mongs.play.client.publisher.vo;

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
    private Double strengthPercent;
    private Double satietyPercent;
    private Double healthyPercent;
    private Double sleepPercent;
}
