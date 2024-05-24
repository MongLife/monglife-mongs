package com.mongs.play.module.kafka.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IncreaseStatusEvent extends BasicEvent{
    private Long mongId;
    private Double addWeight;
    private Double addStrength;
    private Double addSatiety;
    private Double addHealthy;
    private Double addSleep;
}