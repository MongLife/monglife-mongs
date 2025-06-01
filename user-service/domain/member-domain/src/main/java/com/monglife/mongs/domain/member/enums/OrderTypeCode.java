package com.monglife.mongs.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderTypeCode {

    ORDERED("주문 완료"),
    CONSUMED("소비 완료"),
    ;

    private final String description;
}
