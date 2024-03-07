package com.mongs.lifecycle.entity;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Document(collection = "mong_event")
public class MongEvent {
    @Id
    private String eventId;
    private MongEventCode eventCode;
    private Long mongId;
    private Long expiration;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    @Builder.Default
    private EventStatusCode statusCode = EventStatusCode.WAIT;
}
