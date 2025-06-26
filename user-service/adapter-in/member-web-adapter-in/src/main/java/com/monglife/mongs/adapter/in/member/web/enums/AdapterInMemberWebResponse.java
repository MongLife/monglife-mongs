package com.monglife.mongs.adapter.in.member.web.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterInMemberWebResponse implements Response {

    // MEMBER-COLLECTION
    GET_COLLECTION_MAPS(HttpStatus.OK.value(), "100-201-000", "컬렉션 맵 목록 조회에 성공했습니다."),
    GET_COLLECTION_MONGS(HttpStatus.OK.value(), "100-201-001", "컬렉션 몽 목록 조회에 성공했습니다."),

    // MEMBER-FEEDBACK
    CREATE_FEEDBACK(HttpStatus.OK.value(), "100-201-002", "오류 신고 등록에 성공했습니다."),

    // MEMBER-PLAYER
    CREATE_PLAYER(HttpStatus.OK.value(), "100-201-003", "플레이어 등록에 성공했습니다."),
    GET_PLAYER(HttpStatus.OK.value(), "100-201-004", "플레이어 조회에 성공했습니다."),
    BUY_SLOT(HttpStatus.OK.value(), "100-201-005", "추가 슬롯 구매에 성공했습니다."),
    EXCHANGE_STAR_POINT(HttpStatus.OK.value(), "100-201-006", "스타 포인트 환전에 성공했습니다."),

    // MEMBER-STORE
    GET_IN_APP_PRODUCTS(HttpStatus.OK.value(), "100-201-007", "구글 인앱 상품 조회에 성공했습니다."),
    CONSUME_ORDER(HttpStatus.OK.value(), "100-201-008", "주문 소비에 성공했습니다."),
    GET_CONSUMED_ORDERS(HttpStatus.OK.value(), "100-201-009", "소비된 주문 목록 조회에 성공했습니다."),

    // MEMBER-NOTICE
    GET_NOTICE(HttpStatus.OK.value(), "100-201-010", "공지 사항 조회에 성공했습니다."),
    GET_NOTICES(HttpStatus.OK.value(), "100-201-011", "공지 사항 목록 조회에 성공했습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
