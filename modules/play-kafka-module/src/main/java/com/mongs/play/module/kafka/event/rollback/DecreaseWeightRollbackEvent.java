package com.mongs.play.module.kafka.event.rollback;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongs.play.module.kafka.event.BasicEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DecreaseWeightRollbackEvent extends BasicEvent {
    private String message;
    private Integer count;
}