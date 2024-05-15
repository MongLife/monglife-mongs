package com.mongs.play.module.kafka.event.managementWorker;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongs.play.module.kafka.event.BasicEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DecreaseStatusScheduleEvent extends BasicEvent {
    private Long mongId;
    private Double subWeight;
    private Double subStrength;
    private Double subSatiety;
    private Double subHealthy;
    private Double subSleep;
}