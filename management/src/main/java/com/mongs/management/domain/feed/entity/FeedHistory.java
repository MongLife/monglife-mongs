package com.mongs.management.domain.feed.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Document(collection = "feed_history")
public class FeedHistory {
        @Id
        private String id;
        private Long mongId;
        private String code;
        private Integer price;
        private LocalDateTime buyAt;

        @Builder
        public FeedHistory(Long mongId, String code, Integer price) {
                this.id = UUID.randomUUID().toString();
                this.mongId = mongId;
                this.code = code;
                this.price = price;
                this.buyAt = LocalDateTime.now().plusHours(9);
        }

        public LocalDateTime getBuyAt() {
                return this.buyAt.minusHours(9);
        }
}
