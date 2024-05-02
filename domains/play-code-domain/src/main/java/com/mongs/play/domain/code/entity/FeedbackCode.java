package com.mongs.play.domain.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
@Document(collection = "feedback_code")
public record FeedbackCode(
        @Id
        String code,
        String groupCode,
        String message,
        String buildVersion
) {
}
