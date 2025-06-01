package com.monglife.mongs.adapter.in.mong.web.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterInMongWebResponse implements Response {

    GET_TRAINING_TYPES(HttpStatus.OK.value(), "100-101-000", "훈련 타입 목록 조회에 성공했습니다."),
    GET_TRAINING_TYPE(HttpStatus.OK.value(), "100-101-001", "훈련 타입 조회에 성공헀습니다."),
    TRAINING_END(HttpStatus.OK.value(), "100-101-002", "훈련 완료 처리에 성공했습니다."),

    GET_FOODS(HttpStatus.OK.value(), "100-101-003", "음식 목록 조회에 성공했습니다."),
    GET_SNACKS(HttpStatus.OK.value(), "100-101-004", "간식 목록 조회에 성공했습니다."),
    FEED_FOOD(HttpStatus.OK.value(), "100-101-005", "음식 섭취에 성공했습니다."),
    FEED_SNACK(HttpStatus.OK.value(), "100-101-006", "간식 섭취에 성공했습니다."),
    GET_INVENTORIES(HttpStatus.OK.value(), "100-101-007", "인벤토리 목록 조회에 성공했습니다."),
    USE_INVENTORY(HttpStatus.OK.value(), "100-101-008", "인벤토리 아이템 소비에 성공했습니다."),
    BUY_RANDOM_DRAW_TICKET(HttpStatus.OK.value(), "100-101-009", "랜덤 뽑기 티켓 구매에 성공했습니다."),
    RANDOM_DRAW(HttpStatus.OK.value(), "100-101-010", "랜덤 뽑기에 성공했습니다."),

    GET_MONGS(HttpStatus.OK.value(), "100-101-011", "몽 목록 조회에 성공했습니다."),
    GET_MONG(HttpStatus.OK.value(), "100-101-012", "몽 조회에 성공했습니다."),
    CREATE_MONG(HttpStatus.OK.value(), "100-101-013", "몽 생성에 성공했습니다."),
    DELETE_MONG(HttpStatus.OK.value(), "100-101-014", "몽 삭제에 성공했습니다."),
    STROKE_MONG(HttpStatus.OK.value(), "100-101-015", "몽 쓰다듬기에 성공했습니다."),
    SLEEP_WAKEUP_MONG(HttpStatus.OK.value(), "100-101-016", "몽 수면/기상 처리에 성공했습니다."),
    POOP_CLEAN_MONG(HttpStatus.OK.value(), "100-101-017", "몽 배변처리에 성공했습니다."),
    EVOLUTION_MONG(HttpStatus.OK.value(), "100-101-018", "몽 진화에 성공했습니다."),
    GRADUATE_MONG(HttpStatus.OK.value(), "100-101-019", "몽 졸업에 성공했습니다."),
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
