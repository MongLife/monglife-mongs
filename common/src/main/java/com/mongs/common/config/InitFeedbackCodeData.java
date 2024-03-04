package com.mongs.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitFeedbackCodeData {
    /* 캐릭터 */
    FB0000("FB0000", "캐릭터 생성"),
    FB0001("FB0001", "캐릭터 삭제"),
    FB0002("FB0002", "캐릭터 성장"),
    FB0003("FB0003", "캐릭터 선택"),
    /* 음식 */
    FB0100("FB0100", "음식 구매"),
    FB0101("FB0101", "음식 구매 후 스텟 적용"),
    /* 포인트 */
    FB0200("FB0200", "포인트 사용"),
    FB0201("FB0200", "포인트 충전"),

    FB9999("FB9999", "사용자 정의 피드백"),
    ;

    private final String code;
    private final String message;
}
