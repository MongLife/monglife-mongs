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
public class DecreaseStatusEvent extends BasicEvent {
    private Long mongId;
    private Double subWeight;
    private Double subStrength;
    private Double subSatiety;
    private Double subHealthy;
    private Double subSleep;
}
