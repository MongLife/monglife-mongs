package com.mongs.common.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Document(collection = "code_version")
public record CodeVersion(
        @Id
        String buildVersion,
        LocalDateTime createdAt
) {
}
