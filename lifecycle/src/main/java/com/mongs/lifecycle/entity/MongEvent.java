package com.mongs.lifecycle.entity;

import com.mongs.lifecycle.code.MongEventCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "mong_event")
public class MongEvent {
    @Id
    private String eventId;
    private MongEventCode eventCode;
    private Long mongId;
    private LocalDateTime expiredAt;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
