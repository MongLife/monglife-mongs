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
public class IncreaseStatusScheduleEvent extends BasicEvent {
    private Long mongId;
    private Double addWeight;
    private Double addStrength;
    private Double addSatiety;
    private Double addHealthy;
    private Double addSleep;
}