package com.mongs.play.module.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
@Document(collection = "mong_code")
public record MongCode(
        @Id
        String code,
        String name,
        Integer level,
        Integer evolutionPoint,
        String buildVersion
) {
}
