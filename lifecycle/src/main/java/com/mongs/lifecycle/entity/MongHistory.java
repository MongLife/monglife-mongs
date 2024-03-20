package com.mongs.lifecycle.entity;

import com.mongs.core.enums.management.MongActive;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "mong_history")
public class MongHistory {
        @Id
        @Builder.Default
        private String id = UUID.randomUUID().toString();
        private Long mongId;
        private MongActive code;
        private String message;
        @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();
}
