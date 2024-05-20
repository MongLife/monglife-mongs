package com.mongs.play.module.kafka.event.managementInternal;

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
public class IncreaseStatusEvent extends BasicEvent {
    private Long mongId;
    private Double weight;
    private Double strength;
    private Double satiety;
    private Double healthy;
    private Double sleep;
}