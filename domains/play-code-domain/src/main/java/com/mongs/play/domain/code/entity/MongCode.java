package com.mongs.play.domain.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
@Document(collection = "mong_code")
public record MongCode(
        @Id
        String code,
        String name,
        String buildVersion
) {
}
