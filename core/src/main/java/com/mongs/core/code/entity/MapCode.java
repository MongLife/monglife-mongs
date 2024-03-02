package com.mongs.core.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "map_code")
public record MapCode(
        @Id
        String code,
        String name
) {
}