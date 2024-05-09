package com.mongs.play.module.kafka.event.rollback;

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
public class EvolutionMongRollbackEvent extends BasicEvent {
    private Long accountId;
    private String mongCode;
}