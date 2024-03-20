package com.mongs.management.domain.feedHistory.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "feed_history")
public class FeedHistory {
        @Id
        @Builder.Default
        private String id = UUID.randomUUID().toString();
        private Long mongId;
        private String code;
        private Integer price;
        @Builder.Default
        private LocalDateTime buyAt = LocalDateTime.now();
}
