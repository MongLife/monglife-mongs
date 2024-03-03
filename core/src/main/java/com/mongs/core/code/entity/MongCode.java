package com.mongs.core.code.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "mong_code")
public record MongCode(
        @Id
        String code,
        String name
        
        // TODO("몽 진화 조건, 정의 필요")
) {
}
