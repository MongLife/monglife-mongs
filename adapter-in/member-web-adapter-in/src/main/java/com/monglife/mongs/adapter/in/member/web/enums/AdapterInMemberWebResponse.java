package com.monglife.mongs.adapter.in.member.web.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterInMemberWebResponse implements Response {

    // MEMBER-COLLECTION
    CREATE_COLLECTION_MAP(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-000", "컬렉션 맵 등록에 성공했습니다."),
    GET_COLLECTION_MAPS(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-001", "컬렉션 맵 목록 조회에 성공했습니다."),
    GET_COLLECTION_MONGS(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-002", "컬렉션 몽 목록 조회에 성공했습니다."),

    // MEMBER-FEEDBACK
    CREATE_FEEDBACK(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-100", "오류 신고 등록에 성공했습니다."),

    // MEMBER-PLAYER
    CREATE_PLAYER(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-200", "플레이어 등록에 성공했습니다."),
    GET_PLAYER(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-201", "플레이어 조회에 성공했습니다."),
    BUY_SLOT(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-202", "추가 슬롯 구매에 성공했습니다."),
    EXCHANGE_STAR_POINT(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-203", "스타 포인트 환전에 성공했습니다."),

    // MEMBER-STORE
    GET_IN_APP_PRODUCTS(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-300", "구글 인앱 상품 조회에 성공했습니다."),
    CONSUME_ORDER(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-301", "주문 소비에 성공했습니다."),
    GET_CONSUMED_ORDERS(HttpStatus.OK.value(), "ADAPTER-IN-WEB-MEMBER-302", "소비된 주문 목록 조회에 성공했습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto() {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }

}
