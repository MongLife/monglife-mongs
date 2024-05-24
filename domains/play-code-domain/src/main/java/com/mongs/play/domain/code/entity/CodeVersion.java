package com.mongs.play.domain.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Document(collection = "code_version")
public record CodeVersion(
        @Id
        String buildVersion,
        String codeIntegrity,
        Boolean mustUpdateApp,
        LocalDateTime createdAt
) {
}
