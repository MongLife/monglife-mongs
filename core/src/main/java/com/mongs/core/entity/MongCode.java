package com.mongs.core.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "mong_code")
public record MongCode(
        @Id
        String code,
        String name,
        Long version
) {
}
