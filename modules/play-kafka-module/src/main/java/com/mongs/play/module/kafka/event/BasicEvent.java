package com.mongs.play.module.kafka.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BasicEvent {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private String topic;
}

