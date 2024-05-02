package com.mongs.play.app.common.internal.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackCodeData {

    /* 캐릭터 */
    FB_CHARACTER_0000("FB_CHARACTER_0000", "FB_CHARACTER", "캐릭터 생성"),
    FB_CHARACTER_0001("FB_CHARACTER_0001", "FB_CHARACTER", "캐릭터 삭제"),
    FB_CHARACTER_0002("FB_CHARACTER_0002", "FB_CHARACTER", "캐릭터 성장"),
    FB_CHARACTER_0003("FB_CHARACTER_0003", "FB_CHARACTER", "캐릭터 선택"),
    /* 음식 */
    FB_FOOD_0000("FB_FOOD_0000", "FB_FOOD", "음식 구매"),
    FB_FOOD_0001("FB_FOOD_0001", "FB_FOOD", "음식 구매 후 스텟 적용"),
    /* 포인트 */
    FB_POINT_0000("FB_POINT_0000", "FB_POINT", "포인트 사용"),
    FB_POINT_0001("FB_POINT_0001", "FB_POINT", "포인트 충전"),

    FB_CUSTOM_0000("FB_CUSTOM_0000", "FB_CUSTOM", "사용자 정의 피드백"),
    ;

    private final String code;
    private final String groupCode;
    private final String message;
}
