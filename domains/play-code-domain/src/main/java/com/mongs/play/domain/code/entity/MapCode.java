package com.mongs.play.domain.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
@Document(collection = "map_code")
public record MapCode(
        @Id
        String code,
        String name,
        String buildVersion
) {
}
