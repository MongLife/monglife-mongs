package com.mongs.play.module.kafka.event.commit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mongs.play.module.kafka.event.BasicEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DecreaseWeightCommitEvent extends BasicEvent {
    private String message;
    private Integer count;
}