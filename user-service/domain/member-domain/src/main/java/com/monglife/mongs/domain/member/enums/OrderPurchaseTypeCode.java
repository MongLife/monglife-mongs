package com.monglife.mongs.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderPurchaseTypeCode {

    PAYED("지불 완료"),
    CANCEL("지불 취소"),
    PENDING("지불 대기"),
    ;

    private final String description;
}
